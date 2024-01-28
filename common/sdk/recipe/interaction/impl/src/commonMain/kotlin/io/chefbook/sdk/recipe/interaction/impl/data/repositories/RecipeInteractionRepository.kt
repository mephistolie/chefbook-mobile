package io.chefbook.sdk.recipe.interaction.impl.data.repositories

import io.chefbook.libs.utils.result.EmptyResult

internal interface RecipeInteractionRepository {

  suspend fun setRecipeScore(recipeId: String, score: Int?): EmptyResult

  suspend fun setRecipeSavedStatus(recipeId: String, saved: Boolean): EmptyResult

  suspend fun setRecipeFavouriteStatus(recipeId: String, favourite: Boolean): EmptyResult

  suspend fun setRecipeCategories(recipeId: String, categories: List<String>): EmptyResult
}
