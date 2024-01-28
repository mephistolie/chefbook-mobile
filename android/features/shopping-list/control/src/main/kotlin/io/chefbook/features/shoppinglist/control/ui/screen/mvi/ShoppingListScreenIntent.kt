package io.chefbook.features.shoppinglist.control.ui.screen.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed interface ShoppingListScreenIntent : MviIntent {
  data object CreatePurchase : ShoppingListScreenIntent
  data class OpenPurchaseInput(val purchaseId: String) : ShoppingListScreenIntent
  data class SwitchPurchasedStatus(val purchasedId: String) : ShoppingListScreenIntent
  data object RemovePurchasedItems : ShoppingListScreenIntent
  data class OpenRecipe(val recipeId: String) : ShoppingListScreenIntent
  data object Close : ShoppingListScreenIntent
}
