package io.chefbook.sdk.encryption.vault.impl.domain.usecases

import io.chefbook.libs.encryption.AES_SALT_SIZE
import io.chefbook.libs.utils.hash.sha1
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.UnlockEncryptedVaultUseCase
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository

internal class UnlockEncryptedVaultUseCaseImpl(
  private val profileId: String,
  private val encryptionRepository: EncryptedVaultRepository,
) : UnlockEncryptedVaultUseCase {

  override suspend operator fun invoke(password: String): EmptyResult {
    val salt = profileId.sha1.encodeToByteArray().copyOf(AES_SALT_SIZE)
    return encryptionRepository.unlockEncryptedVault(password, salt)
  }
}
