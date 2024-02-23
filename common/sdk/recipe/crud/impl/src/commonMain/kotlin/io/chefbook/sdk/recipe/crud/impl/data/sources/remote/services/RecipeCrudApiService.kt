package io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services

import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.network.api.internal.service.dto.responses.VersionResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.CreateRecipeResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.GetRecipeResponse
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud.RecipeInputRequest

internal interface RecipeCrudApiService {

  suspend fun getRandomRecipe(): Result<GetRecipeResponse>

  suspend fun createRecipe(body: RecipeInputRequest): Result<CreateRecipeResponse>

  suspend fun getRecipe(
    recipeId: String,
    userLanguage: String? = null,
    translated: Boolean = false,
  ): Result<GetRecipeResponse>

  suspend fun updateRecipe(recipeId: String, body: RecipeInputRequest): Result<VersionResponse>

  suspend fun deleteRecipe(recipeId: String): Result<MessageResponse>
}
