package io.chefbook.sdk.encryption.vault.api.external.domain.usecases

import io.chefbook.sdk.encryption.vault.api.external.domain.entities.EncryptedVaultState
import kotlinx.coroutines.flow.Flow

interface ObserveEncryptedVaultStateUseCase {
  operator fun invoke(): Flow<EncryptedVaultState?>
}