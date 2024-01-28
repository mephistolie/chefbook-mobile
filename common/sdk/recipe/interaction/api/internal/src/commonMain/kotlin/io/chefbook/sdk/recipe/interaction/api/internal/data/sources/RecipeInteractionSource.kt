package io.chefbook.sdk.recipe.interaction.api.internal.data.sources

import io.chefbook.libs.utils.result.EmptyResult

interface RecipeInteractionSource {
  suspend fun setRecipeScore(recipeId: String, score: Int?): EmptyResult
  suspend fun setRecipeFavouriteStatus(recipeId: String, isFavourite: Boolean): EmptyResult
  suspend fun setRecipeCategories(recipeId: String, categories: List<String>): EmptyResult
}
