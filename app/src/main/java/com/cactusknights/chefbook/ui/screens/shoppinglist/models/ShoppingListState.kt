package com.cactusknights.chefbook.ui.screens.shoppinglist.models

import com.cactusknights.chefbook.ui.screens.shoppinglist.models.presentation.ShoppingListSection

sealed class ShoppingListState {

    object Loading : ShoppingListState()

    data class Success(
        val shoppingList: List<ShoppingListSection>,
        val hasPurchasedItems: Boolean,
    ) : ShoppingListState()

    object Error: ShoppingListState()
}
