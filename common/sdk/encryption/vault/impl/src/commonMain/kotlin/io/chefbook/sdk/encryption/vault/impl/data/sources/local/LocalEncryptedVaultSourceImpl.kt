package io.chefbook.sdk.encryption.vault.impl.data.sources.local

import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.io.IOProvider
import io.chefbook.libs.exceptions.NotFoundException
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import kotlinx.coroutines.withContext
import okio.BufferedSource

internal class LocalEncryptedVaultSourceImpl(
  profileId: String,
  io: IOProvider,
  private val dispatchers: AppDispatchers,
) : LocalEncryptedVaultSource {

  private val files = io.fileSystem
  private val encryptionDir = io.filesDir.resolve("encryption/$profileId")
  private val vaultKeyPath = encryptionDir.resolve("vault_key")

  override suspend fun getEncryptedVaultKey(): Result<ByteArray> = withContext(dispatchers.io) {
    if (!files.exists(vaultKeyPath)) return@withContext Result.failure(NotFoundException())
    return@withContext Result.success(files.read(vaultKeyPath, BufferedSource::readByteArray))
  }

  override suspend fun setEncryptedVaultKey(privateKey: ByteArray) = withContext(dispatchers.io) {
    return@withContext try {
      files.createDirectories(encryptionDir)
      files.write(vaultKeyPath) { write(privateKey) }
      successResult
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  override suspend fun deleteEncryptedVault(): EmptyResult = withContext(dispatchers.io) {
    return@withContext try {
      if (files.exists(vaultKeyPath)) {
        files.deleteRecursively(vaultKeyPath)
      }
      successResult
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
