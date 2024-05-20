package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.SignInGoogleUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository
import io.chefbook.sdk.core.api.internal.data.repositories.LocalDataRepository
import io.chefbook.sdk.settings.api.external.domain.entities.ProfileMode
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository

internal class SignInGoogleUseCaseImpl(
  private val authRepository: AuthRepository,
  private val settingsRepository: SettingsRepository,
  private val localDataRepository: LocalDataRepository,
) : SignInGoogleUseCase {

  override suspend operator fun invoke(idToken: String) =
    authRepository.signInGoogle(idToken)
      .onSuccess {
        settingsRepository.setProfileMode(ProfileMode.ONLINE)
        localDataRepository.refreshData()
      }
}
