package io.chefbook.sdk.auth.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface ResetPasswordUseCase {

  suspend operator fun invoke(userId: String, code: String, newPassword: String): EmptyResult
}
