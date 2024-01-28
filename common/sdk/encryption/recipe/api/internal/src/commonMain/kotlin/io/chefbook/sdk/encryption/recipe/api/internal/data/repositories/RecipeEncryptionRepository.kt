package io.chefbook.sdk.encryption.recipe.api.internal.data.repositories

import io.chefbook.libs.encryption.AsymmetricPrivateKey
import io.chefbook.libs.encryption.AsymmetricPublicKey
import io.chefbook.libs.encryption.SymmetricKey
import io.chefbook.libs.utils.result.EmptyResult

interface RecipeEncryptionRepository {

  suspend fun generateRecipeKey(): SymmetricKey

  suspend fun getRecipeKey(recipeId: String, vaultKey: AsymmetricPrivateKey): Result<SymmetricKey>

  suspend fun setRecipeKey(recipeId: String, recipeKey: SymmetricKey, vaultKey: AsymmetricPublicKey): EmptyResult

  suspend fun deleteRecipeKey(recipeId: String): EmptyResult

  suspend fun clearLocalData(): EmptyResult
}
