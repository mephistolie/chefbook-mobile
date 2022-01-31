package com.cactusknights.chefbook.screens.main.fragments.shoppinglist.models

import com.cactusknights.chefbook.models.Purchase

sealed class ShoppingListScreenEvent {
    class AddPurchase(val purchase: Purchase) : ShoppingListScreenEvent()
    class MovePurchase(val from: Int, val to: Int) : ShoppingListScreenEvent()
    class ChangePurchasedStatus(val purchaseIndex: Int) : ShoppingListScreenEvent()
    class DeletePurchase(val purchaseIndex: Int) : ShoppingListScreenEvent()
    object DeletePurchased : ShoppingListScreenEvent()
    object ConfirmInput : ShoppingListScreenEvent()
}