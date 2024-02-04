package io.chefbook.sdk.encryption.recipe.impl.data.sources.remote.services

import io.chefbook.sdk.encryption.recipe.impl.data.sources.remote.services.dto.GetRecipeKeyResponse
import io.chefbook.sdk.encryption.recipe.impl.data.sources.remote.services.dto.UploadRecipeKeyRequest
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse

internal interface RecipeEncryptionApiService {

  suspend fun getRecipeKey(recipeId: String): Result<GetRecipeKeyResponse>

  suspend fun uploadRecipeKey(
    recipeId: String,
    body: UploadRecipeKeyRequest
  ): Result<MessageResponse>

  suspend fun deleteRecipeKey(recipeId: String): Result<MessageResponse>
}
