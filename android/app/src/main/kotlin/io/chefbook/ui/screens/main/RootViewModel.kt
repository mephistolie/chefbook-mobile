package io.chefbook.ui.screens.main

import androidx.lifecycle.viewModelScope
import io.chefbook.libs.coroutines.collectIn
import io.chefbook.libs.mvi.StateSideEffectViewModel
import io.chefbook.sdk.auth.api.external.domain.usecases.ObserveCurrentProfileIdUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.ObserveSettingsUseCase
import io.chefbook.ui.screens.main.mvi.RootEffect
import io.chefbook.ui.screens.main.mvi.RootState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class RootViewModel(
  private val observeSettingsUseCase: ObserveSettingsUseCase,
  private val observeCurrentProfileIdUseCase: ObserveCurrentProfileIdUseCase,
//  private val observeProfileDeletionUseCase: ObserveProfileDeletionUseCase,
) : StateSideEffectViewModel<RootState, RootEffect>() {

  override val _state: MutableStateFlow<RootState> = MutableStateFlow(RootState())

  init {
    observeAppState()
  }

  private fun observeAppState() {
    observeCurrentProfileIdUseCase().collectIn(viewModelScope) { profileId ->
      _effect.emit(
        when (profileId) {
          null -> RootEffect.SignedOut
          else -> RootEffect.SignedIn(profileId = profileId)
        }
      )
    }

    observeSettingsUseCase().collectIn(viewModelScope) { settings ->
      _state.update { state -> state.copy(theme = settings.appTheme) }
    }
  }
}
