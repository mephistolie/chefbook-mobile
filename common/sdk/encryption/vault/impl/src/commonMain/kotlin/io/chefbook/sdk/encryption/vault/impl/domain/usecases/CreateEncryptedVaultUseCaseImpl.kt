package io.chefbook.sdk.encryption.vault.impl.domain.usecases

import io.chefbook.libs.crypto.encryption.AesSaltSize
import io.chefbook.libs.crypto.digest.sha1
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.CreateEncryptedVaultUseCase
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository

internal class CreateEncryptedVaultUseCaseImpl(
  private val profileId: String,
  private val encryptionRepository: EncryptedVaultRepository,
) : CreateEncryptedVaultUseCase {

  override suspend operator fun invoke(password: String): EmptyResult {
    val salt = profileId.sha1.copyOf(AesSaltSize)
    return encryptionRepository.createEncryptedVault(password, salt)
  }
}
