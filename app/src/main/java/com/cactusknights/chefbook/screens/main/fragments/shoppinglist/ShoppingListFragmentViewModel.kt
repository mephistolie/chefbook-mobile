package com.cactusknights.chefbook.screens.main.fragments.shoppinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.usecases.ShoppingListUseCases
import com.cactusknights.chefbook.models.Purchase
import com.cactusknights.chefbook.models.ShoppingList
import com.cactusknights.chefbook.screens.main.fragments.shoppinglist.models.ShoppingListScreenEvent
import com.cactusknights.chefbook.screens.main.fragments.shoppinglist.models.ShoppingListScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ShoppingListFragmentViewModel @Inject constructor(
    private val shoppingListUseCases: ShoppingListUseCases
) : ViewModel(), EventHandler<ShoppingListScreenEvent> {

    private val _shoppingListState: MutableStateFlow<ShoppingListScreenState> = MutableStateFlow(ShoppingListScreenState.Loading)
    val shoppingListState: StateFlow<ShoppingListScreenState> = _shoppingListState.asStateFlow()

    private var shoppingList = ShoppingList(purchases = arrayListOf())

    init {
        viewModelScope.launch {
            shoppingListUseCases.listenToShoppingList().collect { updateShoppingListState(it) }
        }
    }

    override fun obtainEvent(event: ShoppingListScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is ShoppingListScreenEvent.AddPurchase -> addPurchase(event.purchase)
                is ShoppingListScreenEvent.MovePurchase -> movePurchase(event.from, event.to)
                is ShoppingListScreenEvent.ChangePurchasedStatus -> changePurchasedStatus(event.purchaseIndex)
                is ShoppingListScreenEvent.DeletePurchase -> deletePurchase(event.purchaseIndex)
                is ShoppingListScreenEvent.DeletePurchased -> deletePurchased()
                is ShoppingListScreenEvent.ConfirmInput -> shoppingListUseCases.syncShoppingList().collect{}
            }
        }
    }


    private fun changePurchasedStatus(purchaseIndex: Int) {
        viewModelScope.launch {
            shoppingList.purchases[purchaseIndex].isPurchased = !shoppingList.purchases[purchaseIndex].isPurchased
            shoppingListUseCases.setShoppingList(shoppingList).collect{}
            updateShoppingListState(shoppingList)
        }
    }

    private suspend fun addPurchase(purchase: Purchase) {
        shoppingListUseCases.addToShoppingList(arrayListOf(purchase)).collect {}
    }

    private suspend fun deletePurchase(purchaseIndex: Int) {
        shoppingList = ShoppingList(shoppingList.purchases.filterIndexed { index, _ ->  index != purchaseIndex })
        shoppingListUseCases.setShoppingList(shoppingList).collect {}
    }

    private suspend fun movePurchase(from: Int, to: Int) {
        Collections.swap(shoppingList.purchases, from, to)
        shoppingListUseCases.setShoppingList(shoppingList).collect {}
    }

    private suspend fun deletePurchased() {
        shoppingList.purchases = shoppingList.purchases.filter{ !it.isPurchased } as ArrayList<Purchase>
        shoppingListUseCases.setShoppingList(shoppingList).collect {}
    }

    private suspend fun updateShoppingListState(newShoppingList: ShoppingList?) {
        if (newShoppingList != null) {
            shoppingList = ShoppingList(newShoppingList.purchases.sortedBy { it.isPurchased })
            _shoppingListState.emit(ShoppingListScreenState.ShoppingListUpdated(shoppingList = shoppingList))
        }
    }
}