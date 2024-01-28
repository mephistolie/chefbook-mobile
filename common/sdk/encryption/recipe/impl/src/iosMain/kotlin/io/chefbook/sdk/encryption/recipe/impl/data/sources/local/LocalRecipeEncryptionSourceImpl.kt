package io.chefbook.sdk.encryption.recipe.impl.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.encryption.recipe.impl.data.sources.RecipeEncryptionSource

internal class LocalRecipeEncryptionSourceImpl() : RecipeEncryptionSource {

  override suspend fun getRecipeKey(recipeId: String): Result<ByteArray> {
    TODO()
  }

  override suspend fun setRecipeKey(recipeId: String, key: ByteArray): EmptyResult {
    TODO()
  }

  override suspend fun deleteRecipeKey(recipeId: String): EmptyResult {
    TODO()
  }
}
