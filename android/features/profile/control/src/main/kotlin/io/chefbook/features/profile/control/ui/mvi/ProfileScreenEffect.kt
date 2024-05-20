package io.chefbook.features.profile.control.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed interface ProfileScreenEffect : MviSideEffect {
  data object Back : ProfileScreenEffect
  data object RequestLogout : ProfileScreenEffect
  data object ProfileEditingScreenOpened : ProfileScreenEffect
  data object AppSettingsScreenOpen : ProfileScreenEffect
  data object AboutAppScreenOpened : ProfileScreenEffect
  data class UrlOpened(val url: String) : ProfileScreenEffect
}
