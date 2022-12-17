package com.cactusknights.chefbook.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.settings.domain.entities.Tab
import com.cactusknights.chefbook.ui.screens.home.models.HomeEffect
import com.cactusknights.chefbook.ui.screens.home.models.HomeEvent
import com.cactusknights.chefbook.ui.screens.home.models.HomeState
import com.mysty.chefbook.core.mvi.EventHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel(), EventHandler<HomeEvent> {

    private val _homeState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    private val _homeEffect = MutableSharedFlow<HomeEffect>()
    val homeEffect = _homeEffect.asSharedFlow()

    override fun obtainEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeEvent.OpenRecipeBook -> {
                    _homeState.emit(homeState.value.copy(currentTab = Tab.RECIPE_BOOK))
                    _homeEffect.emit(HomeEffect.RecipeBookOpened)
                }
                is HomeEvent.OpenShoppingList -> {
                    _homeState.emit(homeState.value.copy(currentTab = Tab.SHOPPING_LIST))
                    _homeEffect.emit(HomeEffect.ShoppingListOpened)
                }
                is HomeEvent.OpenProfile -> _homeEffect.emit(HomeEffect.ProfileOpened)
                else -> {}
            }
        }
    }

}
