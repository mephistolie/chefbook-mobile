package io.chefbook.sdk.encryption.vault.impl.domain.usecases

import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.LockEncryptedVaultUseCase
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository

internal class LockEncryptedVaultUseCaseImpl(
  private val encryptionRepository: EncryptedVaultRepository,
) : LockEncryptedVaultUseCase {

  override suspend operator fun invoke() = encryptionRepository.lockEncryptedVault()
}
