package com.mysty.chefbook.features.recipe.control.ui.navigation

import com.mysty.chefbook.navigation.navigators.IBaseNavigator
import com.mysty.chefbook.navigation.params.dialogs.TwoButtonsDialogParams

interface IRecipeControlScreenNavigator : IBaseNavigator {
    fun openTwoButtonsDialog(
        params: TwoButtonsDialogParams,
        request: String,
    )
    fun openRecipeInputScreen(recipeId: String?)
}
