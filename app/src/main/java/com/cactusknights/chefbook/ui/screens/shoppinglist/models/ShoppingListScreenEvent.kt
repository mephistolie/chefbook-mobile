package com.cactusknights.chefbook.ui.screens.shoppinglist.models

sealed class ShoppingListScreenEvent {
    data class SwitchPurchasedStatus(val purchasedId: String) : ShoppingListScreenEvent()
    object RemovePurchasedItems : ShoppingListScreenEvent()
    data class OpenRecipe(val recipeId: String) : ShoppingListScreenEvent()
}
