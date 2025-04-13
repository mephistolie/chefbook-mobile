package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.RestoreProfileUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.SessionsRepository
import io.chefbook.sdk.core.api.internal.data.repositories.LocalDataRepository
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository

internal class RestoreProfileUseCaseImpl(
  private val profile: ProfileRepository,
  private val sessions: SessionsRepository,
  private val localDataRepo: LocalDataRepository,
) : RestoreProfileUseCase {

  override suspend operator fun invoke() = profile.cancelProfileDeletion()
    .onSuccess {
      sessions.refreshTokens()
      localDataRepo.refreshData()
    }
}
