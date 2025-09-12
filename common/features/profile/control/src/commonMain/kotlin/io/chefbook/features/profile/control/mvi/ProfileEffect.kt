package io.chefbook.features.profile.control.mvi

sealed interface ProfileEffect {
  data object Back : ProfileEffect
  data object RequestLogout : ProfileEffect
  data object ProfileEditingScreenOpened : ProfileEffect
  data object AppSettingsScreenOpen : ProfileEffect
  data object AboutAppScreenOpened : ProfileEffect
  data class UrlOpened(val url: String) : ProfileEffect
}
