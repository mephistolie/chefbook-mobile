package io.chefbook.features.shoppinglist.control.navigation

import io.chefbook.navigation.navigators.BaseNavigator
import io.chefbook.ui.common.presentation.RecipeScreenPage

interface ShoppingListScreenNavigator : BaseNavigator {
  fun openPurchaseInput(shoppingListId: String, purchaseId: String)
  fun openRecipeScreen(
    recipeId: String,
    initPage: RecipeScreenPage = RecipeScreenPage.INGREDIENTS,
    openExpanded: Boolean = false,
  )
}
