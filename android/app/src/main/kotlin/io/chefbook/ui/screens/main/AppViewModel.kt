package io.chefbook.ui.screens.main

import androidx.lifecycle.viewModelScope
import io.chefbook.libs.mvi.StateSideEffectViewModel
import io.chefbook.sdk.auth.api.external.domain.usecases.ObserveProfileDeletionUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.ObserveSettingsUseCase
import io.chefbook.ui.screens.main.mvi.AppEffect
import io.chefbook.ui.screens.main.mvi.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import okhttp3.OkHttpClient

class AppViewModel(
  private val observeSettingsUseCase: ObserveSettingsUseCase,
  private val observeProfileUseCase: ObserveProfileUseCase,
  private val observeProfileDeletionUseCase: ObserveProfileDeletionUseCase,
) : StateSideEffectViewModel<AppState, AppEffect>() {

  override val _state: MutableStateFlow<AppState> = MutableStateFlow(AppState())

  init {
    observeAppState()
  }

  private fun observeAppState() {
    combine(
      observeProfileUseCase(),
      observeProfileDeletionUseCase(),
      observeSettingsUseCase(),
    ) { profile, deletionTimestamp, settings ->
      _state.emit(
        AppState(
          isSignedIn = profile != null && deletionTimestamp == null,
          theme = settings.appTheme
        )
      )
      if (profile == null || deletionTimestamp != null) _effect.emit(AppEffect.SignedOut)
    }
      .launchIn(viewModelScope)
  }
}
