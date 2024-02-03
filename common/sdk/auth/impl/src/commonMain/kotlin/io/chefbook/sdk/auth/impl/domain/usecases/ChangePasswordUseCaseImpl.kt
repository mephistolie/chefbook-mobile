package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.ChangePasswordUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository

internal class ChangePasswordUseCaseImpl(
  private val authRepository: AuthRepository,
) : ChangePasswordUseCase {

  override suspend operator fun invoke(oldPassword: String, newPassword: String) =
    authRepository.changePassword(oldPassword, newPassword)
}
