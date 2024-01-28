package io.chefbook.sdk.encryption.vault.impl.domain.usecases

import io.chefbook.sdk.encryption.vault.api.external.domain.entities.EncryptedVaultState
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.ObserveEncryptedVaultStateUseCase
import kotlinx.coroutines.flow.onStart

internal class ObserveEncryptedVaultStateUseCaseImpl(
  private val encryptionRepository: EncryptedVaultRepository,
) : ObserveEncryptedVaultStateUseCase {

  override operator fun invoke() = encryptionRepository.observeEncryptedVaultState()
}
