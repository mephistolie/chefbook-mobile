package io.chefbook.features.settings.ui

import androidx.lifecycle.viewModelScope
import io.chefbook.sdk.settings.api.external.domain.usecases.ObserveSettingsUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.SetAppIconUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.SetAppThemeUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.SetOpenSavedRecipeExpandedUseCase
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.features.settings.ui.mvi.SettingsScreenEffect
import io.chefbook.features.settings.ui.mvi.SettingsScreenIntent
import io.chefbook.features.settings.ui.mvi.SettingsScreenState
import io.chefbook.sdk.settings.api.external.domain.usecases.SetEnvironmentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


internal typealias ISettingsScreenViewModel = MviViewModel<SettingsScreenState, SettingsScreenIntent, SettingsScreenEffect>

internal class SettingsScreenViewModel(
  observeSettingsUseCase: ObserveSettingsUseCase,
  val setThemeUseCase: SetAppThemeUseCase,
  val setIconUseCase: SetAppIconUseCase,
  val setOpenSavedRecipeExpandedUseCase: SetOpenSavedRecipeExpandedUseCase,
  val setEnvironmentUseCase: SetEnvironmentUseCase,
) : BaseMviViewModel<SettingsScreenState, SettingsScreenIntent, SettingsScreenEffect>() {

  override val _state: MutableStateFlow<SettingsScreenState> =
    MutableStateFlow(SettingsScreenState())

  init {
    viewModelScope.launch {
      observeSettingsUseCase()
        .collect { settings -> _state.emit(SettingsScreenState(settings = settings)) }
    }
  }

  override suspend fun reduceIntent(intent: SettingsScreenIntent) {
    when (intent) {
      is SettingsScreenIntent.Back -> _effect.emit(SettingsScreenEffect.Closed)
      is SettingsScreenIntent.SetTheme -> setThemeUseCase(intent.theme)
      is SettingsScreenIntent.SetIcon -> setIconUseCase(intent.icon)
      is SettingsScreenIntent.SetOpenSavedRecipeExpanded -> setOpenSavedRecipeExpandedUseCase(intent.expand)
      is SettingsScreenIntent.SetEnvironment -> {
        setEnvironmentUseCase(intent.environment)
        _effect.emit(SettingsScreenEffect.AppRestarted)
      }
    }
  }
}
