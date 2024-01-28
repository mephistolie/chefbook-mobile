package io.chefbook.sdk.settings.impl.domain.usecases

import io.chefbook.sdk.settings.api.external.domain.entities.AppIcon
import io.chefbook.sdk.settings.api.external.domain.usecases.SetAppIconUseCase
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository
import io.chefbook.sdk.settings.impl.data.platform.IconSwitcher

internal class SetAppIconUseCaseImpl(
  private val iconSwitcher: IconSwitcher,
  private val settingsRepository: SettingsRepository,
) : SetAppIconUseCase {

  override suspend operator fun invoke(icon: AppIcon) {
    iconSwitcher.switchIconVisibility(icon, isEnabled = true)
    settingsRepository.setAppIcon(icon)
  }
}