package com.cactusknights.chefbook.screens.main.fragments.shoppinglist

import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.base.EventHandler
import com.cactusknights.chefbook.base.StateViewModel
import com.cactusknights.chefbook.domain.usecases.ShoppingListUseCases
import com.cactusknights.chefbook.models.Purchase
import com.cactusknights.chefbook.models.ShoppingList
import com.cactusknights.chefbook.screens.main.fragments.shoppinglist.models.ShoppingListEvent
import com.cactusknights.chefbook.screens.main.fragments.shoppinglist.models.ShoppingListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class ShoppingListFragmentViewModel @Inject constructor(
    private val shoppingListUseCases: ShoppingListUseCases
) : StateViewModel<ShoppingListState>(ShoppingListState()), EventHandler<ShoppingListEvent> {

    init {
        viewModelScope.launch {
            viewModelScope.launch {
                getShoppingList()
            }
            shoppingListUseCases.listenToShoppingList().collect { updateShoppingListState(it) }
        }
    }

    override fun obtainEvent(event: ShoppingListEvent) {
        viewModelScope.launch {
            when (event) {
                is ShoppingListEvent.AddPurchase -> addPurchase(event.purchase)
                is ShoppingListEvent.MovePurchase -> movePurchase(event.from, event.to)
                is ShoppingListEvent.ChangePurchasedStatus -> changePurchasedStatus(event.purchaseIndex)
                is ShoppingListEvent.DeletePurchase -> deletePurchase(event.purchaseIndex)
                is ShoppingListEvent.DeletePurchased -> deletePurchased()
                is ShoppingListEvent.ConfirmInput -> shoppingListUseCases.syncShoppingList().collect{}
            }
        }
    }

    private suspend fun getShoppingList() {
        shoppingListUseCases.getShoppingList().collect { return@collect }
    }

    private fun changePurchasedStatus(purchaseIndex: Int) {
        viewModelScope.launch {
            _state.value.shoppingList.purchases[purchaseIndex].isPurchased =
                !state.value.shoppingList.purchases[purchaseIndex].isPurchased
            shoppingListUseCases.setShoppingList(_state.value.shoppingList).collect{}
            updateShoppingListState(state.value.shoppingList)
        }
    }

    private suspend fun addPurchase(purchase: Purchase) {
        shoppingListUseCases.addToShoppingList(arrayListOf(purchase)).collect {}
    }

    private suspend fun deletePurchase(purchaseIndex: Int) {
        val newShoppingList = ShoppingList(state.value.shoppingList.purchases.filterIndexed { index, _ ->  index != purchaseIndex })
        shoppingListUseCases.setShoppingList(newShoppingList).collect {}
    }

    private suspend fun movePurchase(from: Int, to: Int) {
        Collections.swap(state.value.shoppingList.purchases, from, to)
        shoppingListUseCases.setShoppingList(state.value.shoppingList).collect {}
    }

    private suspend fun deletePurchased() {
        state.value.shoppingList.purchases = state.value.shoppingList.purchases.filter{ !it.isPurchased } as ArrayList<Purchase>
        shoppingListUseCases.setShoppingList(state.value.shoppingList).collect {}
    }

    private suspend fun updateShoppingListState(shoppingList: ShoppingList) {
        val newShoppingList = ShoppingList(shoppingList.purchases.sortedBy { it.isPurchased })
        _state.emit(ShoppingListState(shoppingList = newShoppingList))
    }
}