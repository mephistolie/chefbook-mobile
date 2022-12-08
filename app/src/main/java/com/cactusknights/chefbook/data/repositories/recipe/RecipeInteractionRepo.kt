package com.cactusknights.chefbook.data.repositories.recipe

import com.cactusknights.chefbook.data.ILocalRecipeInteractionSource
import com.cactusknights.chefbook.data.IRemoteRecipeInteractionSource
import com.cactusknights.chefbook.data.repositories.SourceRepo
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.entities.action.AppError
import com.cactusknights.chefbook.domain.entities.action.AppErrorType
import com.cactusknights.chefbook.domain.entities.action.Failure
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.isSuccess
import com.cactusknights.chefbook.domain.interfaces.IRecipeBookCacheWriter
import com.cactusknights.chefbook.domain.interfaces.IRecipeInteractionRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeInteractionRepo @Inject constructor(
    @Local private val localSource: ILocalRecipeInteractionSource,
    @Remote private val remoteSource: IRemoteRecipeInteractionSource,

    private val cache: IRecipeBookCacheWriter,
    private val sourceRepo: SourceRepo,
) : IRecipeInteractionRepo {

    override suspend fun setRecipeSavedStatus(recipeId: Int, saved: Boolean): SimpleAction {
        if (!sourceRepo.isOnlineMode()) return Failure(AppError(AppErrorType.LOCAL_USER))
        val result = if (saved) remoteSource.addRecipeToRecipeBook(recipeId) else remoteSource.removeFromRecipeToRecipeBook(recipeId)

        if (result.isSuccess()) cache.setRecipeSavedStatus(recipeId, saved)

        return result
    }

    override suspend fun setRecipeLikeStatus(recipeId: Int, liked: Boolean): SimpleAction {
        if (!sourceRepo.isOnlineMode()) return Failure(AppError(AppErrorType.LOCAL_USER))
        val result = remoteSource.setRecipeLikeStatus(recipeId, liked)

        if (result.isSuccess()) {
            cache.setRecipeLikeStatus(recipeId, liked)
            if (sourceRepo.isOnlineMode()) localSource.setRecipeLikeStatus(recipeId, liked)
        }

        return result
    }

    override suspend fun setRecipeFavouriteStatus(recipeId: Int, favourite: Boolean): SimpleAction {
        val result = if (sourceRepo.isOnlineMode()) {
            remoteSource.setRecipeFavouriteStatus(recipeId, favourite)
        } else {
            localSource.setRecipeFavouriteStatus(recipeId, favourite)
        }

        if (result.isSuccess()) {
            cache.setRecipeFavouriteStatus(recipeId, favourite)
            if (sourceRepo.isOnlineMode()) localSource.setRecipeFavouriteStatus(recipeId, favourite)
        }

        return result
    }

    override suspend fun setRecipeCategories(recipeId: Int, categories: List<Int>): SimpleAction {
        val result = if (sourceRepo.isOnlineMode()) {
            remoteSource.setRecipeCategories(recipeId, categories)
        } else {
            localSource.setRecipeCategories(recipeId, categories)
        }

        if (result.isSuccess()) {
            cache.setRecipeCategories(recipeId, categories)
            if (sourceRepo.isOnlineMode()) localSource.setRecipeCategories(recipeId, categories)
        }

        return result
    }

}
