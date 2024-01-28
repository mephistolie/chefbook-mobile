package io.chefbook.sdk.encryption.recipe.impl.data.sources

import io.chefbook.libs.utils.result.EmptyResult

internal interface RecipeEncryptionSource {

  suspend fun getRecipeKey(recipeId: String): Result<ByteArray>

  suspend fun setRecipeKey(recipeId: String, key: ByteArray): EmptyResult

  suspend fun deleteRecipeKey(recipeId: String): EmptyResult
}
