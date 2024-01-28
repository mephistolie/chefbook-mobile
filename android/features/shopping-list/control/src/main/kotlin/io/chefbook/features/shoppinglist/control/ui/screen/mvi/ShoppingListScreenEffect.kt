package io.chefbook.features.shoppinglist.control.ui.screen.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed interface ShoppingListScreenEffect : MviSideEffect {
  data object ModalSheetOpened : ShoppingListScreenEffect
  data class OnRecipeOpened(val recipeId: String) : ShoppingListScreenEffect
  data object Closed : ShoppingListScreenEffect
}
