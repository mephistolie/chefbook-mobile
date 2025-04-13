package io.chefbook.sdk.encryption.vault.impl.domain.usecases

import io.chefbook.libs.encryption.AES_SALT_SIZE
import io.chefbook.libs.utils.hash.sha1
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.auth.api.internal.data.repositories.SessionRepository
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.CreateEncryptedVaultUseCase
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository

internal class CreateEncryptedVaultUseCaseImpl(
  private val profileId: String,
  private val encryptionRepository: EncryptedVaultRepository,
) : CreateEncryptedVaultUseCase {

  override suspend operator fun invoke(password: String): EmptyResult {
    val salt = profileId.sha1.encodeToByteArray().copyOf(AES_SALT_SIZE)
    return encryptionRepository.createEncryptedVault(password, salt)
  }
}
