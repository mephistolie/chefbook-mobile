package com.cactusknights.chefbook.ui.screens.shoppinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.shoppinglist.domain.usecases.IAddToShoppingListUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.IObserveShoppingListUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.IRemovePurchasedItemsUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.ISwitchPurchaseStatusUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.ISyncShoppingListUseCase
import com.cactusknights.chefbook.ui.screens.shoppinglist.models.ShoppingListEffect
import com.cactusknights.chefbook.ui.screens.shoppinglist.models.ShoppingListScreenEvent
import com.cactusknights.chefbook.ui.screens.shoppinglist.models.ShoppingListState
import com.cactusknights.chefbook.ui.screens.shoppinglist.models.presentation.ShoppingListSectionMapper
import com.mysty.chefbook.core.mvi.EventHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ShoppingListScreenViewModel(
    private val observeShoppingListUseCase: IObserveShoppingListUseCase,
    private val switchPurchaseStatusUseCase: ISwitchPurchaseStatusUseCase,
    private val removePurchasedItemsUseCase: IRemovePurchasedItemsUseCase,
    private val syncShoppingListUseCase: ISyncShoppingListUseCase,
    private val addToShoppingListUseCase: IAddToShoppingListUseCase,
) : ViewModel(), EventHandler<ShoppingListScreenEvent> {

    private val mutex = Mutex()

    private val _state: MutableStateFlow<ShoppingListState> = MutableStateFlow(ShoppingListState.Loading)
    val state: StateFlow<ShoppingListState> = _state.asStateFlow()

    private val _effect: MutableSharedFlow<ShoppingListEffect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 0)
    val effect: SharedFlow<ShoppingListEffect> = _effect.asSharedFlow()

    init {
        observeShoppingList()
        monitorChanges()
    }

    private fun observeShoppingList() {
        viewModelScope.launch {
            observeShoppingListUseCase()
                .onEach { shoppingList ->
                    val sections = ShoppingListSectionMapper.map(shoppingList)
                    _state.emit(ShoppingListState.Success(
                        shoppingList = sections,
                        hasPurchasedItems = shoppingList.purchases.any { it.isPurchased }
                    ))
                }
                .collect()
        }
    }

    private fun monitorChanges() {
        viewModelScope.launch {
            while (this.isActive) {
                delay(SYNC_THRESHOLD)
                syncChanges()
            }
        }
    }

    override fun obtainEvent(event: ShoppingListScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is ShoppingListScreenEvent.SwitchPurchasedStatus -> mutex.withLock { switchPurchaseStatusUseCase(event.purchasedId) }
                is ShoppingListScreenEvent.RemovePurchasedItems -> mutex.withLock { removePurchasedItemsUseCase() }
                is ShoppingListScreenEvent.OpenRecipe -> _effect.emit(ShoppingListEffect.OnRecipeOpened(event.recipeId))
            }
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            syncShoppingListUseCase()
        }
        super.onCleared()
    }

    private suspend fun syncChanges() = mutex.withLock { syncShoppingListUseCase() }

    companion object {
        private const val SYNC_THRESHOLD = 8000L
    }
}
