package io.chefbook.sdk.encryption.vault.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface DeleteEncryptedVaultUseCase {
  suspend operator fun invoke(): EmptyResult
}