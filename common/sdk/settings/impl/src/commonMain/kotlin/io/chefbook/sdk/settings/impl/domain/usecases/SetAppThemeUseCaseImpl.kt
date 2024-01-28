package io.chefbook.sdk.settings.impl.domain.usecases

import io.chefbook.sdk.settings.api.external.domain.entities.AppTheme
import io.chefbook.sdk.settings.api.external.domain.usecases.SetAppThemeUseCase
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository

internal class SetAppThemeUseCaseImpl(
  private val settingsRepository: SettingsRepository,
) : SetAppThemeUseCase {

  override suspend operator fun invoke(theme: AppTheme) =
    settingsRepository.setAppTheme(theme)
}