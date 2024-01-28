package io.chefbook.features.profile.control.ui

import androidx.lifecycle.viewModelScope
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.features.profile.control.ui.mvi.ProfileScreenEffect
import io.chefbook.features.profile.control.ui.mvi.ProfileScreenIntent
import io.chefbook.features.profile.control.ui.mvi.ProfileScreenState
import io.chefbook.sdk.auth.api.external.domain.usecases.SignOutUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

private const val GOOGLE_PLAY_PAGE = "https://play.google.com/store/apps/details?id=com.cactusknights.chefbook"

internal typealias IProfileScreenViewModel = MviViewModel<ProfileScreenState, ProfileScreenIntent, ProfileScreenEffect>

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
      is ProfileScreenIntent.Logout -> signOutUseCase()
      is ProfileScreenIntent.OpenAppSettingsScreen -> _effect.emit(ProfileScreenEffect.OpenAppSettingsScreen)
      is ProfileScreenIntent.RateApp -> _effect.emit(ProfileScreenEffect.OpenUrl(GOOGLE_PLAY_PAGE))
      is ProfileScreenIntent.OpenAboutAppScreen -> _effect.emit(ProfileScreenEffect.OpenAboutAppScreen)
    }
  }
}
