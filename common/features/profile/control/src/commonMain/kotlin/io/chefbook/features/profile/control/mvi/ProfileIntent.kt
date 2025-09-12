package io.chefbook.features.profile.control.mvi

sealed interface ProfileIntent {
  data object Back : ProfileIntent
  data object RequestLogout : ProfileIntent
  data object SignOut : ProfileIntent
  data object OpenProfileEditingScreen : ProfileIntent
  data object OpenAppSettingsScreen : ProfileIntent
  data object RateApp : ProfileIntent
  data object OpenAboutAppScreen : ProfileIntent
}
