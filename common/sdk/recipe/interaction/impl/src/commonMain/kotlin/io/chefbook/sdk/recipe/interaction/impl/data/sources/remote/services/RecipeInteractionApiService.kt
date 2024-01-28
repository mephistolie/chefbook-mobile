package io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.services

import io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.services.dto.RateRecipeRequest
import io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.services.dto.SetRecipeCategoriesRequest
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse

internal interface RecipeInteractionApiService {

  suspend fun rateRecipe(recipeId: String, body: RateRecipeRequest): Result<MessageResponse>

  suspend fun saveRecipe(recipeId: String): Result<MessageResponse>

  suspend fun removeRecipeFromRecipeBook(recipeId: String): Result<MessageResponse>

  suspend fun markRecipeFavourite(recipeId: String): Result<MessageResponse>

  suspend fun unmarkRecipeFavourite(recipeId: String): Result<MessageResponse>

  suspend fun setRecipeCategories(
    recipeId: String,
    body: SetRecipeCategoriesRequest,
  ): Result<MessageResponse>
}
