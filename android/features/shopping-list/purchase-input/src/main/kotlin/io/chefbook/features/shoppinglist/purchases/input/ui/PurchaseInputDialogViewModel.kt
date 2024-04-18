package io.chefbook.features.shoppinglist.purchases.input.ui

import androidx.lifecycle.viewModelScope
import io.chefbook.features.shoppinglist.purchases.input.ui.mvi.PurchaseInputDialogEffect
import io.chefbook.features.shoppinglist.purchases.input.ui.mvi.PurchaseInputDialogIntent
import io.chefbook.features.shoppinglist.purchases.input.ui.mvi.PurchaseInputDialogState
import io.chefbook.libs.models.measureunit.MeasureUnit
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.libs.utils.result.onSuccess
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.GetShoppingListUseCase
import io.chefbook.sdk.shoppinglist.api.external.domain.usecases.UpdatePurchaseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class PurchaseInputDialogViewModel(
  private val shoppingListId: String,
  private val purchaseId: String,

  getShoppingListUseCase: GetShoppingListUseCase,
  private val updatePurchaseUseCase: UpdatePurchaseUseCase,
) :
  BaseMviViewModel<PurchaseInputDialogState, PurchaseInputDialogIntent, PurchaseInputDialogEffect>() {


  override val _state: MutableStateFlow<PurchaseInputDialogState> =
    MutableStateFlow(PurchaseInputDialogState())

  init {
    viewModelScope.launch {
      getShoppingListUseCase(shoppingListId)
        .map { it.purchases.find { purchase -> purchase.id == purchaseId } }
        .onSuccess { purchase ->
          if (purchase == null) {
            _effect.emit(PurchaseInputDialogEffect.Closed)
            return@launch
          }
          _state.emit(PurchaseInputDialogState(purchase = purchase))
        }
        .onFailure {
          _effect.emit(PurchaseInputDialogEffect.Closed)
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
            measureUnit = if ((intent.unit as? MeasureUnit.Custom)?.name?.isEmpty() == true) null else intent.unit
          )
        }

      PurchaseInputDialogIntent.Close -> _effect.emit(PurchaseInputDialogEffect.Closed)
    }
  }

  private suspend fun updatePurchase(update: (Purchase) -> Purchase) {
    val updatedPurchase = update(state.value.purchase)
    _state.update { it.copy(purchase = updatedPurchase) }
    updatePurchaseUseCase(shoppingListId, updatedPurchase)
  }
}
