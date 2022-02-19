package com.cactusknights.chefbook.data.sources.remote.datasources.recipes

import com.cactusknights.chefbook.data.RecipeInteractionDataSource
import com.cactusknights.chefbook.data.sources.remote.api.RecipesApi
import javax.inject.Inject
import javax.inject.Singleton

class RemoteRecipeInteractionDataSourceImpl @Inject constructor(
    private val api: RecipesApi
) : RecipeInteractionDataSource {

    override suspend fun setRecipeLikeStatus(recipeId: Int, isLiked: Boolean) {
        if (isLiked) { api.likeRecipe(recipeId.toString()) }
        else { api.unlikeRecipe(recipeId.toString()) }
    }

    override suspend fun setRecipeFavouriteStatus(recipeId: Int, isFavourite: Boolean) {
        if (isFavourite) { api.markRecipeFavourite(recipeId.toString()) }
        else { api.unmarkRecipeFavourite(recipeId.toString()) }
    }

    override suspend fun setRecipeCategories(recipeId: Int, categories: List<Int>) {
        api.setRecipeCategories(recipeId.toString(), categories)
    }
}