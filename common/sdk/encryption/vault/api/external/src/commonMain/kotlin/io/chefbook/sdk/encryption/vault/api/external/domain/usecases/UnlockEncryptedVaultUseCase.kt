package io.chefbook.sdk.encryption.vault.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface UnlockEncryptedVaultUseCase {
  suspend operator fun invoke(password: String): EmptyResult
}
