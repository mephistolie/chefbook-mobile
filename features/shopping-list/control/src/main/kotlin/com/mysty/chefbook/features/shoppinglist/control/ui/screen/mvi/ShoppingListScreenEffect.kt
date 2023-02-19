package com.mysty.chefbook.features.shoppinglist.control.ui.screen.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

internal sealed class ShoppingListScreenEffect : MviSideEffect {
  data class OnPurchaseInputOpen(val purchaseId: String) : ShoppingListScreenEffect()
  data class OnRecipeOpened(val recipeId: String) : ShoppingListScreenEffect()
}
