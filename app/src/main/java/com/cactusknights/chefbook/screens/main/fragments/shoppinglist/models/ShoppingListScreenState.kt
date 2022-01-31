package com.cactusknights.chefbook.screens.main.fragments.shoppinglist.models

import com.cactusknights.chefbook.models.ShoppingList

sealed class ShoppingListScreenState {
    object Loading : ShoppingListScreenState()
    data class ShoppingListUpdated(val shoppingList: ShoppingList) : ShoppingListScreenState()
}