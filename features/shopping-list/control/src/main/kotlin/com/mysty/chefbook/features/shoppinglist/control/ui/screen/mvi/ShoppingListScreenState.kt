package com.mysty.chefbook.features.shoppinglist.control.ui.screen.mvi

import com.mysty.chefbook.core.android.mvi.MviState
import com.mysty.chefbook.features.shoppinglist.control.ui.screen.presentation.ShoppingListSection

internal sealed class ShoppingListScreenState : MviState {

    object Loading : ShoppingListScreenState()

    data class Success(
      val shoppingList: List<ShoppingListSection>,
      val hasPurchasedItems: Boolean,
    ) : ShoppingListScreenState()

    object Error: ShoppingListScreenState()
}
