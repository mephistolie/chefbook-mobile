package io.chefbook.sdk.encryption.recipe.impl.data.sources.remote

import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.sdk.encryption.recipe.impl.data.sources.RecipeEncryptionSource
import io.chefbook.sdk.encryption.recipe.impl.data.sources.remote.services.RecipeEncryptionApiService
import io.chefbook.sdk.encryption.recipe.impl.data.sources.remote.services.dto.UploadRecipeKeyRequest
import io.ktor.util.decodeBase64Bytes
import io.ktor.util.encodeBase64
import io.ktor.utils.io.core.toByteArray

internal class RemoteRecipeEncryptionSourceImpl(
  private val api: RecipeEncryptionApiService,
) : RecipeEncryptionSource {

  override suspend fun getRecipeKey(recipeId: String) =
    api.getRecipeKey(recipeId).fold(
      onSuccess = { response ->
        if (response.key == null) return@fold Result.failure(Exception("not found"))
        Result.success(response.key.decodeBase64Bytes())
      },
      onFailure = { Result.failure(it) }
    )

  override suspend fun setRecipeKey(recipeId: String, key: ByteArray) =
    api.uploadRecipeKey(
      recipeId = recipeId,
      body = UploadRecipeKeyRequest(key.encodeBase64())
    ).asEmpty()

  override suspend fun deleteRecipeKey(recipeId: String) =
    api.deleteRecipeKey(recipeId).asEmpty()
}
