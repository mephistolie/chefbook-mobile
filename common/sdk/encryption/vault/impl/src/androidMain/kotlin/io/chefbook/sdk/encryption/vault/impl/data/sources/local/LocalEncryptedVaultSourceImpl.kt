package io.chefbook.sdk.encryption.vault.impl.data.sources.local

import android.content.Context
import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.exceptions.NotFoundException
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

internal class LocalEncryptedVaultSourceImpl(
  private val context: Context,
  private val dispatchers: AppDispatchers,
) : LocalEncryptedVaultSource {

  override suspend fun getEncryptedVaultKey(): Result<ByteArray> = withContext(dispatchers.io) {
    val vaultKeyFile = File(context.filesDir, VAULT_KEY_FILE)
    if (!vaultKeyFile.exists()) return@withContext Result.failure(NotFoundException())
    return@withContext Result.success(vaultKeyFile.readBytes())
  }

  override suspend fun setEncryptedVaultKey(privateKey: ByteArray): EmptyResult = withContext(dispatchers.io) {
    return@withContext try {
      File(context.filesDir, ENCRYPTION_DIR).mkdirs()
      val vaultKeyFile = File(context.filesDir, VAULT_KEY_FILE)
      vaultKeyFile.createNewFile()
      vaultKeyFile.writeBytes(privateKey)
      successResult
    } catch (e: IOException) {
      Result.failure(e)
    }
  }

  override suspend fun deleteEncryptedVault(): EmptyResult = withContext(dispatchers.io) {
    return@withContext try {
      val vaultKeyFile = File(context.filesDir, VAULT_KEY_FILE)
      if (vaultKeyFile.exists()) {
        val deleted = vaultKeyFile.delete()
        if (!deleted) Result.failure<Unit>(Exception("unable to delete vault key file"))
      }
      successResult
    } catch (e: IOException) {
      Result.failure(e)
    }
  }

  companion object {
    const val ENCRYPTION_DIR = "encryption"
    const val VAULT_KEY_FILE = "$ENCRYPTION_DIR/vault_key"
  }
}
