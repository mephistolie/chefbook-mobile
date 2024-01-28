package io.chefbook.ui.screens.main

import androidx.lifecycle.viewModelScope
import io.chefbook.ui.screens.main.mvi.AppEffect
import io.chefbook.ui.screens.main.mvi.AppState
import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.ObserveSettingsUseCase
import io.chefbook.libs.mvi.IStateSideEffectViewModel
import io.chefbook.libs.mvi.StateSideEffectViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

interface IAppViewModel : IStateSideEffectViewModel<AppState, AppEffect> {
  val imageClient: OkHttpClient
}

class AppViewModel(
  private val observeSettingsUseCase: ObserveSettingsUseCase,
  private val observeProfileUseCase: ObserveProfileUseCase,
  override val imageClient: OkHttpClient,
) : StateSideEffectViewModel<AppState, AppEffect>(), IAppViewModel {

  override val _state: MutableStateFlow<AppState> = MutableStateFlow(AppState())

  init {
    viewModelScope.launch {
      launch { observeTheme() }
      launch { observeSession() }
    }
  }

  private suspend fun observeTheme() {
    observeSettingsUseCase()
      .collect { settings -> _state.emit(AppState(theme = settings.appTheme)) }
  }

  // TODO: fix logic
  private suspend fun observeSession() {
    observeProfileUseCase()
      .collect { profile ->
        if (profile == null) {
          _effect.subscriptionCount.collect { if (it > 0) _effect.emit(AppEffect.SignedOut) }
        }
      }
  }
}
