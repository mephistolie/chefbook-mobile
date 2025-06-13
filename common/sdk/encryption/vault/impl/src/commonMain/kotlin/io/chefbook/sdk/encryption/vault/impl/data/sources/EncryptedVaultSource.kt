package io.chefbook.sdk.encryption.vault.impl.data.sources

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.encryption.vault.impl.data.sources.models.EncryptedVaultKey

internal interface EncryptedVaultSource {

  suspend fun getEncryptedVaultKey(): Result<EncryptedVaultKey>

  suspend fun deleteEncryptedVault(): EmptyResult
}
