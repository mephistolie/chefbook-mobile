package io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.services

import io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.services.dto.RateRecipeRequest
import io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.services.dto.SetRecipeCategoriesRequest
import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url

internal class RecipeInteractionApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), RecipeInteractionApiService {

  override suspend fun rateRecipe(
    recipeId: String,
    body: RateRecipeRequest,
  ): Result<MessageResponse> = safePost {
    url("$RECIPES_ROUTE/$recipeId/rate")
    setBody(body)
  }

  override suspend fun saveRecipe(recipeId: String): Result<MessageResponse> = safePost {
    url("$RECIPES_ROUTE/$recipeId/save")
  }

  override suspend fun removeRecipeFromRecipeBook(recipeId: String): Result<MessageResponse> =
    safeDelete {
      url("$RECIPES_ROUTE/$recipeId/save")
    }

  override suspend fun markRecipeFavourite(recipeId: String): Result<MessageResponse> = safePost {
    url("$RECIPES_ROUTE/$recipeId/favourite")
  }

  override suspend fun unmarkRecipeFavourite(recipeId: String): Result<MessageResponse> =
    safeDelete {
      url("$RECIPES_ROUTE/$recipeId/favourite")
    }

  override suspend fun setRecipeCategories(
    recipeId: String,
    body: SetRecipeCategoriesRequest
  ): Result<MessageResponse> = safePut {
    url("$RECIPES_ROUTE/$recipeId/categories")
    setBody(body)
  }

  companion object {
    private const val RECIPES_ROUTE = "/v1/recipes"
  }
}