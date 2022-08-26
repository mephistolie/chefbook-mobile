package com.cactusknights.chefbook.data.repositories.recipes

import com.cactusknights.chefbook.core.coroutines.CoroutineScopes
import com.cactusknights.chefbook.data.ILocalRecipeInteractionSource
import com.cactusknights.chefbook.data.ILocalRecipeSource
import com.cactusknights.chefbook.data.IRemoteRecipeSource
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.AppError
import com.cactusknights.chefbook.domain.entities.action.AppErrorType
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.Failure
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.SuccessResult
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isFailure
import com.cactusknights.chefbook.domain.entities.action.isSuccess
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import com.cactusknights.chefbook.domain.entities.recipe.RecipesFilter
import com.cactusknights.chefbook.domain.entities.recipe.toRecipe
import com.cactusknights.chefbook.domain.entities.recipe.toRecipeInfo
import com.cactusknights.chefbook.domain.interfaces.ICategoryRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeBookCache
import com.cactusknights.chefbook.domain.interfaces.IRecipeRepo
import com.cactusknights.chefbook.domain.interfaces.ISourceRepo
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

@Singleton
class RecipeRepo @Inject constructor(
    @Local private val localSource: ILocalRecipeSource,
    @Remote private val remoteSource: IRemoteRecipeSource,
    @Local private val localInteractionSource: ILocalRecipeInteractionSource,

    private val cache: IRecipeBookCache,
    private val categoriesRepo: ICategoryRepo,
    private val source: ISourceRepo,
    private val scopes: CoroutineScopes,
) : IRecipeRepo {

    private var refreshTimestamp: Long = 0

    override suspend fun observeRecipeBook(): StateFlow<List<RecipeInfo>?> {
        scopes.repository.launch { refreshData() }
        return cache.observeRecipeBook()
    }

    override suspend fun getRecipeBook(forceRefresh: Boolean): List<RecipeInfo> {
        refreshData(forceRefresh)
        return cache.getRecipeBook()
    }

    override suspend fun getRecipesByQuery(query: RecipesFilter) =
        if (source.useRemoteSource()) {
            remoteSource.getRecipesByQuery(query)
        } else {
            Failure(AppError(AppErrorType.LOCAL_USER))
        }

    private suspend fun refreshData(forceRefresh: Boolean = false) {
        if (source.useRemoteSource()) {
            if (forceRefresh || abs(System.currentTimeMillis() - refreshTimestamp) > REFRESH_TIME_THRESHOLD) {

                val localResult = localSource.getRecipeBook()
                if (localResult.isSuccess()) cache.emitRecipeBook(localResult.data())

                val remoteResult = remoteSource.getRecipeBook()
                if (remoteResult.isSuccess()) {
                    cache.emitRecipeBook(remoteResult.data())
                    pullChanges(if (localResult.isSuccess()) localResult.data() else emptyList(), remoteResult.data())
                }

                refreshTimestamp = System.currentTimeMillis()
            }
        } else {
            val localResult = localSource.getRecipeBook()
            if (localResult.isSuccess()) cache.emitRecipeBook(localResult.data())
        }
    }

    private suspend fun pullChanges(local: List<RecipeInfo>, remote: List<RecipeInfo>) {
        categoriesRepo.getCategories()
        for (remoteRecipe in remote) {
            val localRecipe = local.find { it.id == remoteRecipe.id }
            if (localRecipe == null) {
                val result = remoteSource.getRecipe(remoteRecipe.id)
                if (result.isFailure()) continue
                localSource.createRecipe(result.data())
            } else if (remoteRecipe.creationTimestamp < localRecipe.creationTimestamp) {
                val result = remoteSource.getRecipe(remoteRecipe.id)
                if (result.isFailure()) continue
                localSource.updateRecipe(result.data())
            }
            pullRecipeInteractions(localRecipe, remoteRecipe)
        }

        val remoteIds = remote.map { it.id }
        val deletedRecipes = local.filter { it.id !in remoteIds }
        for (recipe in deletedRecipes) {
            localSource.deleteRecipe(recipe.id)
        }
    }

    private suspend fun pullRecipeInteractions(local: RecipeInfo?, remote: RecipeInfo) {
        if (local?.isLiked != remote.isLiked) {
            localInteractionSource.setRecipeLikeStatus(remote.id, remote.isLiked)
        }
        if (local?.likes != remote.likes) {
            localInteractionSource.setRecipeLikes(remote.id, remote.likes)
        }

        if (local?.isFavourite != remote.isFavourite) {
            localInteractionSource.setRecipeFavouriteStatus(remote.id, remote.isFavourite)
        }

        val localCategoriesIds = local?.categories?.map { it.id } ?: emptyList()
        val remoteCategoriesIds = remote.categories.map { it.id }
        if (localCategoriesIds.any { it !in remoteCategoriesIds } || remoteCategoriesIds.any { it !in localCategoriesIds }) {
            localInteractionSource.setRecipeCategories(remote.id, remoteCategoriesIds)
        }
    }

    override suspend fun createRecipe(input: RecipeInput): ActionStatus<Recipe> {
        val result: ActionStatus<Recipe> = if (source.useRemoteSource()) {

            val remoteResult = remoteSource.createRecipe(input)
            if (remoteResult.isSuccess()) {
                val newRecipe = input.toRecipe(id = remoteResult.data())
                localSource.createRecipe(newRecipe)
                DataResult(newRecipe)
            } else remoteResult.asFailure()

        } else {

            val localResult = localSource.createRecipe(input.toRecipe())
            if (localResult.isSuccess()) {
                DataResult(input.toRecipe(id = localResult.data()))
            } else localResult.asFailure()

        }

        if (result.isSuccess()) cache.addRecipe(result.data().toRecipeInfo())

        return result
    }

    override suspend fun getRecipe(recipeId: Int): ActionStatus<Recipe> {
        var result = localSource.getRecipe(recipeId)
        if (result.isFailure() && source.useRemoteSource()) {
            result = remoteSource.getRecipe(recipeId)
        }

        return result
    }

    override suspend fun updateRecipe(recipeId: Int, input: RecipeInput): ActionStatus<Recipe> {
        if (source.useRemoteSource()) {
            val result = remoteSource.updateRecipe(recipeId, input)
            if (result.isFailure()) return result.asFailure()
        }

        val updatedRecipe = input.toRecipe(id = recipeId)
        localSource.updateRecipe(updatedRecipe)
        cache.updateRecipe(updatedRecipe.toRecipeInfo())

        return DataResult(updatedRecipe)
    }

    override suspend fun deleteRecipe(recipeId: Int): SimpleAction {
        if (source.useRemoteSource()) {
            val result = remoteSource.deleteRecipe(recipeId)
            if (result.isFailure()) return result.asFailure()
        }

        localSource.deleteRecipe(recipeId)
        cache.removeRecipe(recipeId)

        return SuccessResult
    }

    override suspend fun setRecipeSavedStatus(recipeId: Int, saved: Boolean): SimpleAction {
        if (!source.isOnlineMode()) return Failure(AppError(AppErrorType.LOCAL_USER))

        val result = if (source.isOnlineMode()) {
            if (saved) {
                remoteSource.addRecipeToRecipeBook(recipeId)
            } else {
                remoteSource.removeFromRecipeToRecipeBook(recipeId)
            }
        } else SuccessResult

        if (result.isSuccess()) {
            scopes.repository.launch {
                if (saved) {
                    val localResult = localSource.getRecipe(recipeId)
                    if (localResult.isSuccess()) {
                        cache.addRecipe(localResult.data().toRecipeInfo())
                    } else {
                        val remoteResult = remoteSource.getRecipe(recipeId)
                        if (remoteResult.isSuccess()) {
                            cache.addRecipe(remoteResult.data().toRecipeInfo())
                            localSource.createRecipe(remoteResult.data())
                        }
                    }
                } else {
                    localSource.deleteRecipe(recipeId)
                    cache.removeRecipe(recipeId)
                }
            }
        }

        return result
    }

    companion object {
        private const val REFRESH_TIME_THRESHOLD = 5 * 60 * 1000
    }

}
