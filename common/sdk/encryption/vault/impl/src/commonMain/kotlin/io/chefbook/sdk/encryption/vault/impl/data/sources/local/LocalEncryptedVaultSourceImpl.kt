package io.chefbook.sdk.encryption.vault.impl.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.api.internal.DatabaseDataSource
import io.chefbook.sdk.database.api.internal.EncryptedVaults
import io.chefbook.sdk.encryption.vault.impl.data.sources.models.EncryptedVaultKey
import io.ktor.util.decodeBase64Bytes
import io.ktor.util.encodeBase64

internal class LocalEncryptedVaultSourceImpl(
  private val profileId: String,
  database: ChefBookDatabase,
) : DatabaseDataSource(), LocalEncryptedVaultSource {

  private val queries = database.encryptedVaultQueries

  override suspend fun getEncryptedVaultKey(): Result<EncryptedVaultKey> = safeQueryResult {
    val result = queries.select(profileId = profileId).executeAsOne()
    return@safeQueryResult EncryptedVaultKey(
      key = result.encryptedKey.decodeBase64Bytes(),
      passwordSalt = result.passwordSalt.decodeBase64Bytes(),
    )
  }

  override suspend fun setEncryptedVaultKey(
    encryptedPrivateKey: ByteArray,
    passwordSalt: ByteArray,
  ): EmptyResult = safeQueryResult {
    queries.insert(
      encryptedVaults = EncryptedVaults(
        profileId = profileId,
        encryptedKey = encryptedPrivateKey.encodeBase64(),
        passwordSalt = passwordSalt.encodeBase64(),
      )
    )
  }

  override suspend fun deleteEncryptedVault(): EmptyResult = safeQueryResult {
    queries.delete(profileId)
  }
}
