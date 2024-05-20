package io.chefbook.sdk.encryption.recipe.impl.data.sources.remote.services

import io.chefbook.sdk.encryption.recipe.impl.data.sources.remote.services.dto.GetRecipeKeyResponse
import io.chefbook.sdk.encryption.recipe.impl.data.sources.remote.services.dto.UploadRecipeKeyRequest
import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url

internal class RecipeEncryptionApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), RecipeEncryptionApiService {

  override suspend fun getRecipeKey(recipeId: String): Result<GetRecipeKeyResponse> =
    safeGet("$RECIPES_ENCRYPTION_ROUTE/$recipeId")

  override suspend fun uploadRecipeKey(
    recipeId: String,
    body: UploadRecipeKeyRequest,
  ): Result<MessageResponse> =
    safePost("$RECIPES_ENCRYPTION_ROUTE/$recipeId") { setBody(body) }

  override suspend fun deleteRecipeKey(recipeId: String): Result<MessageResponse> =
    safeDelete("$RECIPES_ENCRYPTION_ROUTE/$recipeId")

  companion object {
    private const val RECIPES_ENCRYPTION_ROUTE = "v1/encryption/recipes"
  }
}
