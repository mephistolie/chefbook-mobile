package io.chefbook.sdk.encryption.vault.impl.data.sources.remote

import io.chefbook.libs.encryption.AsymmetricPublicKey
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.encryption.vault.impl.data.sources.EncryptedVaultSource

internal interface RemoteEncryptedVaultSource : EncryptedVaultSource {

  suspend fun createEncryptedVault(
    publicKey: AsymmetricPublicKey,
    privateKey: ByteArray
  ): EmptyResult
}
