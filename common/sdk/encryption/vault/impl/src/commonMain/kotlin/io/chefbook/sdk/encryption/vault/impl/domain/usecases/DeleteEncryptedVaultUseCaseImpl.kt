package io.chefbook.sdk.encryption.vault.impl.domain.usecases

import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.DeleteEncryptedVaultUseCase

internal class DeleteEncryptedVaultUseCaseImpl(
  private val encryptionRepository: EncryptedVaultRepository,
) : DeleteEncryptedVaultUseCase {

  override suspend operator fun invoke() = encryptionRepository.deleteEncryptedVault()
}
