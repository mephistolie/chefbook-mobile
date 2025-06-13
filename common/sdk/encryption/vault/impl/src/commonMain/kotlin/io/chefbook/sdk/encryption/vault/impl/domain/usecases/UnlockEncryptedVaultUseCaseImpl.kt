package io.chefbook.sdk.encryption.vault.impl.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.UnlockEncryptedVaultUseCase
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository

internal class UnlockEncryptedVaultUseCaseImpl(
  private val encryptionRepository: EncryptedVaultRepository,
) : UnlockEncryptedVaultUseCase {

  override suspend operator fun invoke(password: String): EmptyResult =
    encryptionRepository.unlockEncryptedVault(password)
}
