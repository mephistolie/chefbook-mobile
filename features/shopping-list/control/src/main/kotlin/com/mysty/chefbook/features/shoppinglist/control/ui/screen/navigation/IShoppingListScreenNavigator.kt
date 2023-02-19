package com.mysty.chefbook.features.shoppinglist.control.ui.screen.navigation

import com.mysty.chefbook.ui.common.presentation.RecipeScreenPage

interface IShoppingListScreenNavigator {
  fun openPurchaseInput(purchaseId: String)
  fun openRecipeScreen(
    recipeId: String,
    initPage: RecipeScreenPage = RecipeScreenPage.INGREDIENTS,
    openExpanded: Boolean = false,
  )
}