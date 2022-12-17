package com.mysty.chefbook.api.recipe.data.repositories

import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.errors.AppError
import com.mysty.chefbook.api.common.communication.errors.AppErrorType
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.recipe.data.cache.IRecipeBookCacheWriter
import com.mysty.chefbook.api.recipe.domain.IRecipeInteractionRepo
import com.mysty.chefbook.api.sources.domain.ISourcesRepo

internal class RecipeInteractionRepo constructor(
    private val localSource: ILocalRecipeInteractionSource,
    private val remoteSource: IRemoteRecipeInteractionSource,

    private val cache: IRecipeBookCacheWriter,
    private val sourceRepo: ISourcesRepo,
) : IRecipeInteractionRepo {

    override suspend fun setRecipeSavedStatus(recipeId: String, saved: Boolean): SimpleAction {
        if (!sourceRepo.isOnlineMode()) return Failure(AppError(AppErrorType.LOCAL_USER))
        val result = if (saved) remoteSource.addRecipeToRecipeBook(recipeId) else remoteSource.removeFromRecipeToRecipeBook(recipeId)

        if (result.isSuccess()) cache.setRecipeSavedStatus(recipeId, saved)

        return result
    }

    override suspend fun setRecipeLikeStatus(recipeId: String, liked: Boolean): SimpleAction {
        if (!sourceRepo.isOnlineMode()) return Failure(AppError(AppErrorType.LOCAL_USER))
        val result = remoteSource.setRecipeLikeStatus(recipeId, liked)

        if (result.isSuccess()) {
            cache.setRecipeLikeStatus(recipeId, liked)
            if (sourceRepo.isOnlineMode()) localSource.setRecipeLikeStatus(recipeId, liked)
        }

        return result
    }

    override suspend fun setRecipeFavouriteStatus(recipeId: String, favourite: Boolean): SimpleAction {
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

    override suspend fun setRecipeCategories(recipeId: String, categories: List<String>): SimpleAction {
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
