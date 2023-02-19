package com.mysty.chefbook.features.recipe.input.navigation

import com.mysty.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect

fun handleBaseRecipeInputScreenEffect(
  effect: RecipeInputScreenEffect,
  navigator: IRecipeInputScreenBaseNavigator
) {
  when (effect) {
    is RecipeInputScreenEffect.OnSaved -> navigator.openSavedDialog()
    is RecipeInputScreenEffect.OnBack -> navigator.navigateUp()
    is RecipeInputScreenEffect.OnClose -> navigator.closeRecipeInput(recipeId = effect.recipeId)
    else -> Unit
  }
}
