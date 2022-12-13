package com.cactusknights.chefbook.data.datasources.remote.recipes

import com.cactusknights.chefbook.data.IRemoteRecipeInteractionSource
import com.cactusknights.chefbook.data.dto.remote.common.toActionStatus
import com.cactusknights.chefbook.data.dto.remote.recipe.RecipeCategoriesRequest
import com.cactusknights.chefbook.data.network.INetworkHandler
import com.cactusknights.chefbook.data.network.api.RecipeApi
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.asEmpty

class RemoteRecipeInteractionSource(
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
