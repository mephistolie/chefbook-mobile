package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.ResetPasswordUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository

internal class ResetPasswordUseCaseImpl(
  private val authRepository: AuthRepository,
) : ResetPasswordUseCase {

  override suspend operator fun invoke(userId: String, code: String, newPassword: String) =
    authRepository.resetPassword(userId, code, newPassword)
}
