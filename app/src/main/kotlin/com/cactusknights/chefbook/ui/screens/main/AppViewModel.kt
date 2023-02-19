package com.cactusknights.chefbook.ui.screens.main

import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.ui.screens.main.mvi.AppEffect
import com.cactusknights.chefbook.ui.screens.main.mvi.AppState
import com.mysty.chefbook.api.profile.domain.usecases.IObserveProfileUseCase
import com.mysty.chefbook.api.settings.domain.usecases.IObserveSettingsUseCase
import com.mysty.chefbook.core.android.mvi.IStateSideEffectViewModel
import com.mysty.chefbook.core.android.mvi.StateSideEffectViewModel
import com.mysty.chefbook.core.coroutines.AppDispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

interface IAppViewModel : IStateSideEffectViewModel<AppState, AppEffect> {
    val imageClient: OkHttpClient
}

class AppViewModel(
    private val observeSettingsUseCase: IObserveSettingsUseCase,
    private val observeProfileUseCase: IObserveProfileUseCase,
    override val imageClient: OkHttpClient,
    dispatchers: AppDispatchers,
) : StateSideEffectViewModel<AppState, AppEffect>(), IAppViewModel {

    override val _state: MutableStateFlow<AppState> = MutableStateFlow(AppState())

    init {
        viewModelScope.launch(context = dispatchers.io) {
            launch { observeTheme() }
            launch { observeSession() }
        }
    }

    private suspend fun observeTheme() {
        observeSettingsUseCase()
            .collect { settings -> _state.emit(AppState(theme = settings.theme)) }
    }

    private suspend fun observeSession() {
        observeProfileUseCase()
            .collect { profile -> if (profile == null) _effect.emit(AppEffect.SignedOut) }
    }

}
