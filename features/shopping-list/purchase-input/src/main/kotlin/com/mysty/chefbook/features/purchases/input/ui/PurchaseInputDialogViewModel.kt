package com.mysty.chefbook.features.purchases.input.ui

import androidx.lifecycle.viewModelScope
import com.mysty.chefbook.api.common.entities.unit.MeasureUnit
import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.IObserveShoppingListUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.ISyncShoppingListUseCase
import com.mysty.chefbook.api.shoppinglist.domain.usecases.IUpdatePurchaseUseCase
import com.mysty.chefbook.core.android.mvi.IMviViewModel
import com.mysty.chefbook.core.android.mvi.MviViewModel
import com.mysty.chefbook.features.purchases.input.ui.mvi.PurchaseInputDialogEffect
import com.mysty.chefbook.features.purchases.input.ui.mvi.PurchaseInputDialogIntent
import com.mysty.chefbook.features.purchases.input.ui.mvi.PurchaseInputDialogState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal typealias IPurchaseInputDialogViewModel =
        IMviViewModel<PurchaseInputDialogState, PurchaseInputDialogIntent, PurchaseInputDialogEffect>

internal class PurchaseInputDialogViewModel(
  private val ingredientId: String,

  private val observeShoppingListUseCase: IObserveShoppingListUseCase,
  private val updatePurchaseUseCase: IUpdatePurchaseUseCase,
  private val syncShoppingListUseCase: ISyncShoppingListUseCase,
) : MviViewModel<PurchaseInputDialogState, PurchaseInputDialogIntent, PurchaseInputDialogEffect>() {

  private val mutex = Mutex()

  override val _state: MutableStateFlow<PurchaseInputDialogState> =
    MutableStateFlow(PurchaseInputDialogState())

  init {
    observeShoppingList()
    monitorChanges()
  }

  private fun observeShoppingList() {
    viewModelScope.launch {
      observeShoppingListUseCase()
        .map { it.purchases.find { it.id == ingredientId } }
        .collect { purchase ->
          if (purchase == null) return@collect _effect.emit(PurchaseInputDialogEffect.Close)
          _state.emit(PurchaseInputDialogState(purchase = purchase))
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

  override suspend fun reduceIntent(intent: PurchaseInputDialogIntent) {
    when (intent) {
      is PurchaseInputDialogIntent.SetName -> updatePurchase { it.copy(name = intent.name) }
      is PurchaseInputDialogIntent.SetAmount ->
        updatePurchase {
          it.copy(amount = if (intent.amount != null && intent.amount > 0) intent.amount else null)
        }
      is PurchaseInputDialogIntent.SetMeasureUnit ->
        updatePurchase {
          it.copy(
            unit = if ((intent.unit as? MeasureUnit.Custom)?.name?.isEmpty() == true) null else intent.unit
          )
        }
      PurchaseInputDialogIntent.Close -> _effect.emit(PurchaseInputDialogEffect.Close)
    }
  }

  private suspend fun updatePurchase(update: (Purchase) -> Purchase) {
    mutex.withLock { updatePurchaseUseCase(update(state.value.purchase)) }
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
