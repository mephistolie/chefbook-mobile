package com.mysty.chefbook.features.home.ui

import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.profile.domain.usecases.IObserveProfileUseCase
import com.mysty.chefbook.api.settings.domain.entities.Tab
import com.mysty.chefbook.core.android.mvi.IMviViewModel
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.features.home.ui.mvi.HomeScreenEffect
import com.mysty.chefbook.features.home.ui.mvi.HomeScreenIntent
import com.mysty.chefbook.features.home.ui.mvi.HomeScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal typealias IHomeViewModel = IMviViewModel<HomeScreenState, HomeScreenIntent, HomeScreenEffect>

internal class HomeViewModel(
  observeProfileUseCase: IObserveProfileUseCase,
) : MviViewModel<HomeScreenState, HomeScreenIntent, HomeScreenEffect>() {

  override val _state: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState())

  init {
    viewModelScope.launch {
      observeProfileUseCase()
        .collect { profile ->
          withSafeState { state ->
            _state.emit(state.copy(profileAvatar = profile?.avatar))
          }
        }
    }
  }

  override suspend fun reduceIntent(intent: HomeScreenIntent) {
    when (intent) {
      is HomeScreenIntent.OpenRecipeBook -> _state.emit(state.value.copy(currentTab = Tab.RECIPE_BOOK))
      is HomeScreenIntent.OpenShoppingList -> _state.emit(state.value.copy(currentTab = Tab.SHOPPING_LIST))
      is HomeScreenIntent.OpenProfile -> _effect.emit(HomeScreenEffect.ProfileOpened)
      else -> Unit
    }
  }

}
