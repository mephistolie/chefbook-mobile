package io.chefbook.features.profile.control.ui.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed interface ProfileScreenIntent : MviIntent {
  data object Back : ProfileScreenIntent
  data object RequestLogout : ProfileScreenIntent
  data object Logout : ProfileScreenIntent
  data object OpenAppSettingsScreen : ProfileScreenIntent
  data object RateApp : ProfileScreenIntent
  data object OpenAboutAppScreen : ProfileScreenIntent
}
