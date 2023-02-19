package com.mysty.chefbook.features.shoppinglist.control.ui.screen.mvi

import com.mysty.chefbook.core.android.mvi.MviIntent

internal sealed class ShoppingListScreenIntent : MviIntent {
    object CreatePurchase : ShoppingListScreenIntent()
    data class OpenPurchaseInput(val purchaseId: String) : ShoppingListScreenIntent()
    data class SwitchPurchasedStatus(val purchasedId: String) : ShoppingListScreenIntent()
    object RemovePurchasedItems : ShoppingListScreenIntent()
    data class OpenRecipe(val recipeId: String) : ShoppingListScreenIntent()
}
