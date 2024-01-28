package io.chefbook.features.profile.control.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed interface ProfileScreenEffect : MviSideEffect {
  data object Back : ProfileScreenEffect
  data object RequestLogout : ProfileScreenEffect
  data object OpenAppSettingsScreen : ProfileScreenEffect
  data object OpenAboutAppScreen : ProfileScreenEffect
  data class OpenUrl(val url: String) : ProfileScreenEffect
}
