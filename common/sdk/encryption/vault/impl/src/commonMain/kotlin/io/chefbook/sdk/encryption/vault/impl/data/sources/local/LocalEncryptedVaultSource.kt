package io.chefbook.sdk.encryption.vault.impl.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.encryption.vault.impl.data.sources.EncryptedVaultSource

internal interface LocalEncryptedVaultSource : EncryptedVaultSource {

  suspend fun setEncryptedVaultKey(privateKey: ByteArray): EmptyResult
}
