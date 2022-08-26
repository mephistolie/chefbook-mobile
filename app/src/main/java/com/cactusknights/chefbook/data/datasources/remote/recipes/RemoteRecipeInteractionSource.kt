package com.cactusknights.chefbook.data.datasources.remote.recipes

import com.cactusknights.chefbook.data.IRecipeInteractionSource
import com.cactusknights.chefbook.data.dto.remote.common.toActionStatus
import com.cactusknights.chefbook.data.dto.remote.recipe.RecipeCategoriesRequest
import com.cactusknights.chefbook.data.network.INetworkHandler
import com.cactusknights.chefbook.data.network.api.RecipeApi
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.asEmpty
import javax.inject.Inject

class RemoteRecipeInteractionSource @Inject constructor(
    private val api: RecipeApi,
    private val handleResponse: INetworkHandler,
) : IRecipeInteractionSource {

    override suspend fun setRecipeLikeStatus(recipeId: Int, isLiked: Boolean): SimpleAction =
        if (isLiked) {
            handleResponse { api.likeRecipe(recipeId) }.toActionStatus().asEmpty()
        } else {
            handleResponse { api.unlikeRecipe(recipeId) }.toActionStatus().asEmpty()
        }

    override suspend fun setRecipeFavouriteStatus(recipeId: Int, isFavourite: Boolean): SimpleAction =
        if (isFavourite) {
            handleResponse { api.markRecipeFavourite(recipeId) }.toActionStatus().asEmpty()
        } else {
            handleResponse { api.unmarkRecipeFavourite(recipeId) }.toActionStatus().asEmpty()
        }

    override suspend fun setRecipeCategories(recipeId: Int, categories: List<Int>): SimpleAction =
        handleResponse { api.setRecipeCategories(recipeId, RecipeCategoriesRequest(categories)) }.toActionStatus().asEmpty()

}