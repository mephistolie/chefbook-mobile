package io.chefbook.ui.screens.main

import androidx.lifecycle.viewModelScope
import io.chefbook.libs.mvi.StateSideEffectViewModel
import io.chefbook.sdk.auth.api.external.domain.usecases.ObserveCurrentProfileIdUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.ObserveSettingsUseCase
import io.chefbook.ui.screens.main.mvi.AppEffect
import io.chefbook.ui.screens.main.mvi.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

class AppViewModel(
  private val observeSettingsUseCase: ObserveSettingsUseCase,
  private val observeCurrentProfileIdUseCase: ObserveCurrentProfileIdUseCase,
//  private val observeProfileDeletionUseCase: ObserveProfileDeletionUseCase,
) : StateSideEffectViewModel<AppState, AppEffect>() {

  override val _state: MutableStateFlow<AppState> = MutableStateFlow(AppState())

  init {
    observeAppState()
  }

  private fun observeAppState() {
    combine(
      observeCurrentProfileIdUseCase(),
//      observeProfileDeletionUseCase(),
      observeSettingsUseCase(),
    ) { profileId, settings ->
      _state.emit(
        AppState(
          profileId = profileId,
          theme = settings.appTheme
        )
      )
      if (profileId == null) _effect.emit(AppEffect.SignedOut)
    }
      .launchIn(viewModelScope)
  }
}
