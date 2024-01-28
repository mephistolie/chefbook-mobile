package io.chefbook.sdk.recipe.interaction.impl.data.sources.remote

import io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.services.RecipeInteractionApiService
import io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.services.dto.RateRecipeRequest
import io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.services.dto.SetRecipeCategoriesRequest
import io.chefbook.libs.utils.result.asEmpty

internal class RemoteRecipeInteractionSourceImpl(
  private val api: RecipeInteractionApiService,
) : RemoteRecipeInteractionSource {

  override suspend fun saveRecipe(recipeId: String) =
    api.saveRecipe(recipeId).asEmpty()

  override suspend fun removeRecipeFromRecipeBook(recipeId: String) =
    api.removeRecipeFromRecipeBook(recipeId).asEmpty()

  override suspend fun setRecipeScore(recipeId: String, score: Int?) =
    api.rateRecipe(recipeId, RateRecipeRequest(score)).asEmpty()

  override suspend fun setRecipeFavouriteStatus(
    recipeId: String,
    isFavourite: Boolean
  ) =
    if (isFavourite) api.markRecipeFavourite(recipeId).asEmpty()
    else api.unmarkRecipeFavourite(recipeId).asEmpty()

  override suspend fun setRecipeCategories(
    recipeId: String,
    categories: List<String>
  ) =
    api.setRecipeCategories(recipeId, SetRecipeCategoriesRequest(categories)).asEmpty()
}
