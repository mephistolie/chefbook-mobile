package io.chefbook.sdk.encryption.vault.impl.domain.usecases

import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.GetEncryptedVaultStateUseCase

internal class GetEncryptedVaultStateUseCaseImpl(
  private val encryptionRepository: EncryptedVaultRepository,
) : GetEncryptedVaultStateUseCase {

  override suspend operator fun invoke() = encryptionRepository.getEncryptedVaultState()

}
