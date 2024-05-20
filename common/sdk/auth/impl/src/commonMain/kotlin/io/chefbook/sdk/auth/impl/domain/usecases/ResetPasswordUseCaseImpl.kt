package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.ResetPasswordUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.PasswordRepository

internal class ResetPasswordUseCaseImpl(
  private val repository: PasswordRepository,
) : ResetPasswordUseCase {

  override suspend operator fun invoke(userId: String, code: String, newPassword: String) =
    repository.resetPassword(userId, code, newPassword)
}
