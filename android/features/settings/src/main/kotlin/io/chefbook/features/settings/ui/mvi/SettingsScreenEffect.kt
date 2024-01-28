package io.chefbook.features.settings.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed interface SettingsScreenEffect : MviSideEffect {
  data object Closed : SettingsScreenEffect
  data object AppRestarted : SettingsScreenEffect
}
