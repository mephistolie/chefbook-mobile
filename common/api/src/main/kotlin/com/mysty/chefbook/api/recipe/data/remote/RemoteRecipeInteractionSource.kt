package com.mysty.chefbook.api.recipe.data.remote

import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.asEmpty
import com.mysty.chefbook.api.common.network.INetworkHandler
import com.mysty.chefbook.api.common.network.dto.toActionStatus
import com.mysty.chefbook.api.recipe.data.remote.api.RecipeApi
import com.mysty.chefbook.api.recipe.data.remote.dto.RecipeCategoriesRequest
import com.mysty.chefbook.api.recipe.data.repositories.IRemoteRecipeInteractionSource

internal class RemoteRecipeInteractionSource(
    private val api: RecipeApi,
    private val handleResponse: INetworkHandler,
) : IRemoteRecipeInteractionSource {

    override suspend fun addRecipeToRecipeBook(recipeId: String): SimpleAction =
        handleResponse { api.addRecipeToRecipeBook(recipeId) }.toActionStatus().asEmpty()

    override suspend fun removeFromRecipeToRecipeBook(recipeId: String): SimpleAction =
        handleResponse { api.removeRecipeFromRecipeBook(recipeId) }.toActionStatus().asEmpty()

    override suspend fun setRecipeLikeStatus(recipeId: String, isLiked: Boolean): SimpleAction =
        if (isLiked) {
            handleResponse { api.likeRecipe(recipeId) }.toActionStatus().asEmpty()
        } else {
            handleResponse { api.unlikeRecipe(recipeId) }.toActionStatus().asEmpty()
        }

    override suspend fun setRecipeFavouriteStatus(recipeId: String, isFavourite: Boolean): SimpleAction =
        if (isFavourite) {
            handleResponse { api.markRecipeFavourite(recipeId) }.toActionStatus().asEmpty()
        } else {
            handleResponse { api.unmarkRecipeFavourite(recipeId) }.toActionStatus().asEmpty()
        }

    override suspend fun setRecipeCategories(recipeId: String, categories: List<String>): SimpleAction =
        handleResponse { api.setRecipeCategories(recipeId, RecipeCategoriesRequest(categories)) }.toActionStatus().asEmpty()

}
