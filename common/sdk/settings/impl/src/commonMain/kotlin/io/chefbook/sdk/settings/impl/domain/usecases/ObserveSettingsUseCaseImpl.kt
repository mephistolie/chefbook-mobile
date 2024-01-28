package io.chefbook.sdk.settings.impl.domain.usecases

import io.chefbook.sdk.settings.api.external.domain.usecases.ObserveSettingsUseCase
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository

internal class ObserveSettingsUseCaseImpl(
  private val settingsRepository: SettingsRepository,
) : ObserveSettingsUseCase {

  override operator fun invoke() = settingsRepository.observeSettings()
}