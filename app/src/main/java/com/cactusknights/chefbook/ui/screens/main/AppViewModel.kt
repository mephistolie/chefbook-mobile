package com.cactusknights.chefbook.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.ui.screens.main.models.AppEffect
import com.cactusknights.chefbook.ui.screens.main.models.AppState
import com.mysty.chefbook.api.profile.domain.usecases.IObserveProfileUseCase
import com.mysty.chefbook.api.settings.domain.usecases.IObserveSettingsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.OkHttpClient

class AppViewModel(
    private val observeSettingsUseCase: IObserveSettingsUseCase,
    private val observeProfileUseCase: IObserveProfileUseCase,
    val imageClient: OkHttpClient,
) : ViewModel() {

    private val _appState: MutableStateFlow<AppState> = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    private val _appEffect: MutableSharedFlow<AppEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val appEffect: SharedFlow<AppEffect> = _appEffect.asSharedFlow()

    private val mutex = Mutex()

    init {
        observeSettings()
        observeProfile()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            observeSettingsUseCase()
                .onEach {
                    mutex.withLock {
                        _appState.emit(appState.value.copy(settings = it))
                    }
                }
                .collect()
        }
    }

    private fun observeProfile() {
        viewModelScope.launch {
            observeProfileUseCase()
                .onEach { profile ->
                    if (profile != null) {
                        mutex.withLock {
                            _appState.emit(appState.value.copy(profile = profile))
                        }
                    } else {
                        _appEffect.emit(AppEffect.SignOut)
                    }
                }
                .collect()
        }
    }

}