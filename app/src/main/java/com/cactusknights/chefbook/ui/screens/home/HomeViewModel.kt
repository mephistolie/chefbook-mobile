package com.cactusknights.chefbook.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.entities.settings.Tab
import com.cactusknights.chefbook.ui.screens.home.models.HomeEvent
import com.cactusknights.chefbook.ui.screens.home.models.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel(), EventHandler<HomeEvent> {

    private val _homeState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()
    val homeEffect = _homeState.asSharedFlow()

    override fun obtainEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeEvent.OpenRecipeBook -> _homeState.emit(homeState.value.copy(currentTab = Tab.RECIPE_BOOK))
                is HomeEvent.OpenShoppingList -> _homeState.emit(homeState.value.copy(currentTab = Tab.SHOPPING_LIST))
                else -> {}
            }
        }
    }

}
