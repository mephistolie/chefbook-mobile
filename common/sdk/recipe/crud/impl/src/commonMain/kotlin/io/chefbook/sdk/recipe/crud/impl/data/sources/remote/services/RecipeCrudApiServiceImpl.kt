package io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services

import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.network.api.internal.service.dto.responses.VersionResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.CreateRecipeResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.GetRecipeResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.GetRecipesRequest
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.GetRecipesResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.RecipeInputRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.client.request.url

internal class RecipeCrudApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), RecipeCrudApiService {
  override suspend fun getRecipes(params: GetRecipesRequest): Result<GetRecipesResponse> = safeGet {
    url(RECIPES_ROUTE)
    setBody(params)
  }

  override suspend fun getRandomRecipe(): Result<GetRecipeResponse> = safeGet {
    url("$RECIPES_ROUTE/random")
  }

  override suspend fun createRecipe(body: RecipeInputRequest): Result<CreateRecipeResponse> = safePost {
    url(RECIPES_ROUTE)
    setBody(body)
  }

  override suspend fun getRecipe(
    recipeId: String,
    userLanguage: String?,
    translated: Boolean,
  ): Result<GetRecipeResponse> = safeGet {
    url("$RECIPES_ROUTE/$recipeId")
    parameter("userLanguage", userLanguage)
    parameter("translated", translated)
  }

  override suspend fun updateRecipe(
    recipeId: String,
    body: RecipeInputRequest,
  ): Result<VersionResponse> = safePut {
    url("$RECIPES_ROUTE/$recipeId")
    setBody(body)
  }

  override suspend fun deleteRecipe(recipeId: String): Result<MessageResponse> = safeDelete {
    url("$RECIPES_ROUTE/$recipeId")
  }

  companion object {
    private const val RECIPES_ROUTE = "/v1/recipes"
  }
}
