package io.chefbook.sdk.encryption.vault.impl.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult

internal class LocalEncryptedVaultSourceImpl() : LocalEncryptedVaultSource {

  override suspend fun getEncryptedVaultKey(): Result<ByteArray> = TODO()

  override suspend fun setEncryptedVaultKey(privateKey: ByteArray): EmptyResult = TODO()

  override suspend fun deleteEncryptedVault(): EmptyResult = TODO()
}
