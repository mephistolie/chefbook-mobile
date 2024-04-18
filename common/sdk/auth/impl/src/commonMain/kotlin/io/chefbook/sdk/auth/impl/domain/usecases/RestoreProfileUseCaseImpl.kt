package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.RestoreProfileUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository
import io.chefbook.sdk.core.api.internal.data.repositories.LocalDataRepository
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository

internal class RestoreProfileUseCaseImpl(
  private val profileRepostiory: ProfileRepository,
  private val authRepository: AuthRepository,
  private val localDataRepository: LocalDataRepository,
) : RestoreProfileUseCase {

  override suspend operator fun invoke() = profileRepostiory.cancelProfileDeletion()
    .onSuccess {
      authRepository.refreshTokens()
      localDataRepository.refreshData()
    }
}
