package io.chefbook.ui.screens.main

import androidx.lifecycle.viewModelScope
import io.chefbook.libs.coroutines.collectIn
import io.chefbook.ui.screens.main.mvi.AppEffect
import io.chefbook.ui.screens.main.mvi.AppState
import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.ObserveSettingsUseCase
import io.chefbook.libs.mvi.IStateSideEffectViewModel
import io.chefbook.libs.mvi.StateSideEffectViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
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
    observeAppState()
  }

  private fun observeAppState() {
    combine(
      observeProfileUseCase(),
      observeSettingsUseCase(),
    ) { profile, settings ->
      _state.emit(
        AppState(
          isSignedIn = profile != null,
          theme = settings.appTheme
        )
      )
      if (profile == null) _effect.emit(AppEffect.SignedOut)
    }
      .launchIn(viewModelScope)
  }
}
