package io.chefbook.sdk.encryption.vault.impl.data.sources

import io.chefbook.libs.utils.result.EmptyResult

internal interface EncryptedVaultSource {

  suspend fun getEncryptedVaultKey(): Result<ByteArray>

  suspend fun deleteEncryptedVault(): EmptyResult
}
