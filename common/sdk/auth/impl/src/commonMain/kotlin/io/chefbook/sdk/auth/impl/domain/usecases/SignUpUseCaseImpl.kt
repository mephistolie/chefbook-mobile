package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.SignUpUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository

internal class SignUpUseCaseImpl(
  private val authRepository: AuthRepository,
) : SignUpUseCase {

  override suspend operator fun invoke(email: String, password: String) =
    authRepository.signUp(email, password)
}
