package io.chefbook.sdk.encryption.recipe.impl.data.repositories

import io.chefbook.libs.encryption.AsymmetricPrivateKey
import io.chefbook.libs.encryption.AsymmetricPublicKey
import io.chefbook.libs.encryption.HybridCryptor
import io.chefbook.libs.encryption.SymmetricKey
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.core.api.internal.data.repositories.DataSourcesRepository
import io.chefbook.sdk.encryption.recipe.api.internal.data.repositories.RecipeEncryptionRepository
import io.chefbook.sdk.encryption.recipe.impl.data.sources.RecipeEncryptionSource
import io.chefbook.sdk.encryption.recipe.impl.data.sources.local.LocalRecipeEncryptionSource

internal class RecipeEncryptionRepositoryImpl(
  private val localSource: LocalRecipeEncryptionSource,
  private val remoteSource: RecipeEncryptionSource,

  private val sources: DataSourcesRepository,
) : RecipeEncryptionRepository {

  override suspend fun generateRecipeKey() = HybridCryptor.generateSymmetricKey()

  override suspend fun getRecipeKey(
    recipeId: String,
    vaultKey: AsymmetricPrivateKey
  ): Result<SymmetricKey> {
    var encryptedKeyResult = localSource.getRecipeKey(recipeId)
    if (encryptedKeyResult.isFailure && sources.isRemoteSourceAvailable()) {
      encryptedKeyResult = remoteSource.getRecipeKey(recipeId)
      if (encryptedKeyResult.isSuccess) localSource.setRecipeKey(
        recipeId,
        encryptedKeyResult.getOrThrow()
      )
    }
    return encryptedKeyResult.map { key -> HybridCryptor.decryptSymmetricKeyByPrivateKey(key, vaultKey) }
  }

  override suspend fun setRecipeKey(
    recipeId: String,
    recipeKey: SymmetricKey,
    vaultKey: AsymmetricPublicKey,
  ): EmptyResult {
    val encryptedRecipeKey = HybridCryptor.encryptSymmetricKeyByPublicKey(recipeKey, vaultKey)
    return if (sources.isRemoteSourceEnabled()) {
      remoteSource.setRecipeKey(recipeId, encryptedRecipeKey)
        .onSuccess {  localSource.setRecipeKey(recipeId, encryptedRecipeKey) }
    } else {
      localSource.setRecipeKey(recipeId, encryptedRecipeKey)
    }
  }

  override suspend fun deleteRecipeKey(recipeId: String): EmptyResult {
    var result = localSource.deleteRecipeKey(recipeId)
    if (sources.isRemoteSourceAvailable()) result = remoteSource.deleteRecipeKey(recipeId)

    return result
  }

  override suspend fun clearLocalData() = localSource.clear()
}
