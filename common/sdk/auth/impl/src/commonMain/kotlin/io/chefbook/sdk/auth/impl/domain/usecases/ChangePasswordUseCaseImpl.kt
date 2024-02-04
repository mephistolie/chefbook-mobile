package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.ChangePasswordUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.PasswordRepository

internal class ChangePasswordUseCaseImpl(
  private val repository: PasswordRepository,
) : ChangePasswordUseCase {

  override suspend operator fun invoke(oldPassword: String, newPassword: String) =
    repository.changePassword(oldPassword, newPassword)
}
