package io.chefbook.sdk.auth.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface ChangePasswordUseCase {

  suspend operator fun invoke(oldPassword: String, newPassword: String): EmptyResult
}
