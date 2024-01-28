package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.sdk.auth.api.external.domain.usecases.SignInUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository
import io.chefbook.sdk.core.api.internal.data.repositories.LocalDataRepository
import io.chefbook.sdk.settings.api.external.domain.entities.ProfileMode
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository

internal class SignInUseCaseImpl(
  private val authRepository: AuthRepository,
  private val settingsRepository: SettingsRepository,
  private val localDataRepository: LocalDataRepository,
) : SignInUseCase {

  override suspend operator fun invoke(login: String, password: String) =
    authRepository.signIn(login, password)
      .onSuccess {
        settingsRepository.setProfileMode(ProfileMode.ONLINE)
        localDataRepository.refreshData()
      }
      .asEmpty()
}
