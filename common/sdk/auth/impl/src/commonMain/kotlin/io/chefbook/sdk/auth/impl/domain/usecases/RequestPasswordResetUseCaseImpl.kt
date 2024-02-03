package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.RequestPasswordResetUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository

internal class RequestPasswordResetUseCaseImpl(
  private val authRepository: AuthRepository,
) : RequestPasswordResetUseCase {

  override suspend operator fun invoke(login: String) =
    authRepository.requestPasswordReset(login)
}
