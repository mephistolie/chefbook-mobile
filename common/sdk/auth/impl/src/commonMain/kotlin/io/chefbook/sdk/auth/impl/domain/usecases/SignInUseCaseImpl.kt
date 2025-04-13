package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.SignInUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository
import io.chefbook.sdk.core.api.internal.data.repositories.LocalDataRepository

internal class SignInUseCaseImpl(
  private val authRepository: AuthRepository,
  private val localDataRepository: LocalDataRepository,
) : SignInUseCase {

  override suspend operator fun invoke(login: String, password: String) =
    authRepository.signIn(login, password)
      .onSuccess {
        localDataRepository.refreshData()
      }
}
