package io.chefbook.sdk.encryption.vault.api.external.domain.usecases

import io.chefbook.sdk.encryption.vault.api.external.domain.entities.EncryptedVaultState

interface GetEncryptedVaultStateUseCase {
  suspend operator fun invoke(): EncryptedVaultState
}
