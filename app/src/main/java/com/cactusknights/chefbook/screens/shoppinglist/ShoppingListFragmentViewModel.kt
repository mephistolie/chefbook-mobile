package com.cactusknights.chefbook.screens.shoppinglist

import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.common.BaseViewModel
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.usecases.ShoppingListUseCases
import com.cactusknights.chefbook.models.Selectable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ShoppingListFragmentViewModel @Inject constructor(
    private val shoppingListUseCases: ShoppingListUseCases
) : BaseViewModel<ShoppingListState>(ShoppingListState()) {

    suspend fun getShoppingList() {
        shoppingListUseCases.getShoppingList().collect { result ->
            when (result) {
                is Result.Success -> {
                    updateShoppingList(result.data!!)
                }
                else -> {
                }
            }
        }
    }

    fun commitChanges() {
        viewModelScope.launch { shoppingListUseCases.setShoppingList(state.value.shoppingList).collect {  } }
    }

    fun changePurchasedStatus(purchaseIndex: Int) {
        viewModelScope.launch {
            _state.value.shoppingList[purchaseIndex].isSelected =
                !state.value.shoppingList[purchaseIndex].isSelected
            updateShoppingList(state.value.shoppingList)
        }
    }

    fun addPurchase(purchase: Selectable<String>) {
        viewModelScope.launch {
            state.value.shoppingList.add(purchase)
            updateShoppingList(state.value.shoppingList)
        }
    }

    fun removePurchase(purchaseIndex: Int) {
        viewModelScope.launch {
            state.value.shoppingList.removeAt(purchaseIndex)
            updateShoppingList(state.value.shoppingList)
        }
    }

    fun movePurchase(from: Int, to: Int) {
        viewModelScope.launch {
            Collections.swap(state.value.shoppingList, from, to)
            updateShoppingList(state.value.shoppingList)
        }
    }

    fun deletePurchased() {
        viewModelScope.launch {
            val newShoppingList = arrayListOf<Selectable<String>>()
            state.value.shoppingList.filter{ !it.isSelected }.toCollection(newShoppingList)
            updateShoppingList(newShoppingList)
        }
    }

    private suspend fun updateShoppingList(shoppingList: ArrayList<Selectable<String>>) {
        val newShoppingList = arrayListOf<Selectable<String>>()
        shoppingList.sortedBy { it.isSelected }.toCollection(newShoppingList)
        _state.emit(
            ShoppingListState(
                shoppingList = newShoppingList
            )
        )
    }
}