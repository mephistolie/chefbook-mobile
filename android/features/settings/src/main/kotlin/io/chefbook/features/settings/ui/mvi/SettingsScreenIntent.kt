package io.chefbook.features.settings.ui.mvi

import io.chefbook.libs.mvi.MviIntent
import io.chefbook.sdk.settings.api.external.domain.entities.AppIcon
import io.chefbook.sdk.settings.api.external.domain.entities.AppTheme
import io.chefbook.sdk.settings.api.external.domain.entities.Environment

internal sealed interface SettingsScreenIntent : MviIntent {
  data object Back : SettingsScreenIntent
  data class SetTheme(val theme: AppTheme) : SettingsScreenIntent
  data class SetIcon(val icon: AppIcon) : SettingsScreenIntent
  data class SetOpenSavedRecipeExpanded(val expand: Boolean) : SettingsScreenIntent
  data class SetEnvironment(val environment: Environment) : SettingsScreenIntent
}
