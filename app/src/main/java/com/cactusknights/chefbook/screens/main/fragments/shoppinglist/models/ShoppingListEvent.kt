package com.cactusknights.chefbook.screens.main.fragments.shoppinglist.models

import com.cactusknights.chefbook.models.Purchase

sealed class ShoppingListEvent {
    class AddPurchase(val purchase: Purchase) : ShoppingListEvent()
    class MovePurchase(val from: Int, val to: Int) : ShoppingListEvent()
    class ChangePurchasedStatus(val purchaseIndex: Int) : ShoppingListEvent()
    class DeletePurchase(val purchaseIndex: Int) : ShoppingListEvent()
    object DeletePurchased : ShoppingListEvent()
    object ConfirmInput : ShoppingListEvent()
}