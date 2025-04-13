package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.SignOutUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository

internal class SignOutUseCaseImpl(
  private val authRepository: AuthRepository,
) : SignOutUseCase {

  override suspend operator fun invoke() {
    authRepository.signOut()
  }
}
