package io.chefbook.ui.delegates

import io.chefbook.sdk.settings.api.external.domain.entities.AppIcon
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository
import io.chefbook.sdk.settings.impl.data.platform.IconSwitcher

class IconSwitcherDelegate(
  private val settingsRepository: SettingsRepository,
  private val iconSwitcher: IconSwitcher,
) {

  fun disableUnselectedIcons() {
    val selectedIcon = settingsRepository.lastSetIcon() ?: return
    AppIcon.entries.forEach { icon ->
      iconSwitcher.switchIconVisibility(icon, isEnabled = icon == selectedIcon)
    }
  }
}
