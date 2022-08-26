package com.cactusknights.chefbook.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.ui.screens.home.models.HomeEffect
import com.cactusknights.chefbook.ui.screens.home.models.HomeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel(), EventHandler<HomeEvent> {

    private val _dashboardEffect: MutableSharedFlow<HomeEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val homeEffect = _dashboardEffect.asSharedFlow()

    override fun obtainEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeEvent.OpenRecipeBook -> _dashboardEffect.emit(HomeEffect.RecipeBookOpened)
                is HomeEvent.OpenShoppingList -> _dashboardEffect.emit(HomeEffect.ShoppingListOpened)
                else -> {}
            }
        }
    }

}
