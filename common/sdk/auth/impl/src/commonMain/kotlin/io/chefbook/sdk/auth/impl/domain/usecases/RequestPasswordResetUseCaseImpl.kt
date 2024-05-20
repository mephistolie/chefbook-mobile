package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.RequestPasswordResetUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.PasswordRepository

internal class RequestPasswordResetUseCaseImpl(
  private val repository: PasswordRepository,
) : RequestPasswordResetUseCase {

  override suspend operator fun invoke(login: String) =
    repository.requestPasswordReset(login)
}
