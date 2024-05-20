package io.chefbook.features.profile.control.ui

import androidx.lifecycle.viewModelScope
import io.chefbook.features.profile.control.ui.mvi.ProfileScreenEffect
import io.chefbook.features.profile.control.ui.mvi.ProfileScreenIntent
import io.chefbook.features.profile.control.ui.mvi.ProfileScreenState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.sdk.auth.api.external.domain.usecases.SignOutUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

private const val GOOGLE_PLAY_PAGE = "https://play.google.com/store/apps/details?id=com.cactusknights.chefbook"

internal class ProfileScreenViewModel(
  observeProfileUseCase: ObserveProfileUseCase,
  private val signOutUseCase: SignOutUseCase,
) : BaseMviViewModel<ProfileScreenState, ProfileScreenIntent, ProfileScreenEffect>() {

  override val _state: MutableStateFlow<ProfileScreenState> = MutableStateFlow(ProfileScreenState())

  init {
    viewModelScope.launch {
      observeProfileUseCase()
        .filterNotNull()
        .collect { profile ->
          _state.emit(ProfileScreenState(profile = profile))
        }
    }
  }

  override suspend fun reduceIntent(intent: ProfileScreenIntent) {
    when (intent) {
      is ProfileScreenIntent.Back -> _effect.emit(ProfileScreenEffect.Back)
      is ProfileScreenIntent.RequestLogout -> _effect.emit(ProfileScreenEffect.RequestLogout)
      is ProfileScreenIntent.SignOut -> signOutUseCase()
      is ProfileScreenIntent.OpenProfileEditingScreen -> _effect.emit(ProfileScreenEffect.ProfileEditingScreenOpened)
      is ProfileScreenIntent.OpenAppSettingsScreen -> _effect.emit(ProfileScreenEffect.AppSettingsScreenOpen)
      is ProfileScreenIntent.RateApp -> _effect.emit(ProfileScreenEffect.UrlOpened(GOOGLE_PLAY_PAGE))
      is ProfileScreenIntent.OpenAboutAppScreen -> _effect.emit(ProfileScreenEffect.AboutAppScreenOpened)
    }
  }
}
