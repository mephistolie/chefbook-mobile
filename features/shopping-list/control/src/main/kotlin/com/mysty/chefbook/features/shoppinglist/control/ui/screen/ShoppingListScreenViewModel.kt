package com.mysty.chefbook.features.shoppinglist.control.ui.screen

import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.shoppinglist.domain.usecases.ICreatePurchaseUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.IObserveShoppingListUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.IRemovePurchasedItemsUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.ISwitchPurchaseStatusUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.ISyncShoppingListUseCase
import com.mysty.chefbook.core.android.mvi.IMviViewModel
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.features.shoppinglist.control.ui.screen.mvi.ShoppingListScreenEffect
import com.mysty.chefbook.features.shoppinglist.control.ui.screen.mvi.ShoppingListScreenIntent
import com.mysty.chefbook.features.shoppinglist.control.ui.screen.mvi.ShoppingListScreenState
import com.mysty.chefbook.features.shoppinglist.control.ui.screen.presentation.ShoppingListSectionMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal typealias IShoppingListScreenViewModel = IMviViewModel<ShoppingListScreenState, ShoppingListScreenIntent, ShoppingListScreenEffect>

internal class ShoppingListScreenViewModel(
  private val observeShoppingListUseCase: IObserveShoppingListUseCase,
  private val switchPurchaseStatusUseCase: ISwitchPurchaseStatusUseCase,
  private val createPurchaseUseCase: ICreatePurchaseUseCase,
  private val removePurchasedItemsUseCase: IRemovePurchasedItemsUseCase,
  private val syncShoppingListUseCase: ISyncShoppingListUseCase,
) : MviViewModel<ShoppingListScreenState, ShoppingListScreenIntent, ShoppingListScreenEffect>() {

  private val mutex = Mutex()

  override val _state: MutableStateFlow<ShoppingListScreenState> = MutableStateFlow(
    ShoppingListScreenState.Loading
  )

  init {
    observeShoppingList()
    monitorChanges()
  }

  private fun observeShoppingList() {
    viewModelScope.launch {
      observeShoppingListUseCase()
        .collect { shoppingList ->
          val sections = ShoppingListSectionMapper.map(shoppingList)
          _state.emit(ShoppingListScreenState.Success(
            shoppingList = sections,
            hasPurchasedItems = shoppingList.purchases.any { it.isPurchased }
          ))
        }
    }
  }

  private fun monitorChanges() {
    viewModelScope.launch {
      while (isActive) {
        delay(SYNC_THRESHOLD)
        syncChanges()
      }
    }
  }

  override suspend fun reduceIntent(intent: ShoppingListScreenIntent) {
    when (intent) {
      is ShoppingListScreenIntent.CreatePurchase -> mutex.withLock {
        val result = createPurchaseUseCase()
        if (result.isSuccess()) _effect.emit(ShoppingListScreenEffect.OnPurchaseInputOpen(result.data()))
      }
      is ShoppingListScreenIntent.OpenPurchaseInput ->
        _effect.emit(ShoppingListScreenEffect.OnPurchaseInputOpen(intent.purchaseId))
      is ShoppingListScreenIntent.SwitchPurchasedStatus ->
        mutex.withLock { switchPurchaseStatusUseCase(intent.purchasedId) }
      is ShoppingListScreenIntent.RemovePurchasedItems -> mutex.withLock { removePurchasedItemsUseCase() }
      is ShoppingListScreenIntent.OpenRecipe ->
        _effect.emit(ShoppingListScreenEffect.OnRecipeOpened(intent.recipeId))
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
