package io.chefbook.features.recipe.control.navigation

import io.chefbook.navigation.navigators.BaseNavigator
import io.chefbook.navigation.params.dialogs.TwoButtonsDialogParams

interface RecipeControlScreenNavigator : BaseNavigator {

  fun openTwoButtonsDialog(params: TwoButtonsDialogParams, request: String)

  fun openRecipeInputScreen(recipeId: String?)
}
