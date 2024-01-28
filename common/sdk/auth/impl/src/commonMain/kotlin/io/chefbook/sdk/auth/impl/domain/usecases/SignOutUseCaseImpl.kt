package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.SignOutUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.CurrentSessionRepository
import io.chefbook.sdk.settings.api.external.domain.entities.ProfileMode
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository

internal class SignOutUseCaseImpl(
  private val sessionRepository: CurrentSessionRepository,
  private val settingsRepository: SettingsRepository,
) : SignOutUseCase {

  override suspend operator fun invoke() {
    settingsRepository.setProfileMode(ProfileMode.UNSPECIFIED)
    sessionRepository.finishSession()
  }
}
