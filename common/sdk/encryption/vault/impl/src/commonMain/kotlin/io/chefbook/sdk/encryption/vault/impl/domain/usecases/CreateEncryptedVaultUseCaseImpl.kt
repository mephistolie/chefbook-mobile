package io.chefbook.sdk.encryption.vault.impl.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.CreateEncryptedVaultUseCase
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository

internal class CreateEncryptedVaultUseCaseImpl(
  private val encryptionRepository: EncryptedVaultRepository,
) : CreateEncryptedVaultUseCase {

  override suspend operator fun invoke(password: String): EmptyResult =
    encryptionRepository.createEncryptedVault(password)
}
