package io.chefbook.sdk.encryption.vault.api.internal.data.repositories

import io.chefbook.libs.encryption.AsymmetricPrivateKey
import io.chefbook.libs.encryption.AsymmetricPublicKey
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.encryption.vault.api.external.domain.entities.EncryptedVaultState
import kotlinx.coroutines.flow.Flow

interface EncryptedVaultRepository {

  fun observeEncryptedVaultState(): Flow<EncryptedVaultState>

  suspend fun getEncryptedVaultState(): EncryptedVaultState

  suspend fun refreshEncryptedVaultState(isEnabled: Boolean): EmptyResult

  suspend fun createEncryptedVault(password: String, salt: ByteArray): EmptyResult

  suspend fun unlockEncryptedVault(password: String, salt: ByteArray): EmptyResult

  suspend fun lockEncryptedVault(): EmptyResult

  suspend fun getVaultPublicKey(): Result<AsymmetricPublicKey>

  suspend fun getVaultPrivateKey(): Result<AsymmetricPrivateKey>

  suspend fun deleteEncryptedVault(): EmptyResult

  suspend fun clearLocalData(): EmptyResult
}
