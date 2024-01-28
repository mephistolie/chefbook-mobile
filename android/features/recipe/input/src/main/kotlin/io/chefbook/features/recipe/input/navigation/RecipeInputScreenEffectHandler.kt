package io.chefbook.features.recipe.input.navigation

import io.chefbook.features.recipe.input.ui.mvi.RecipeInputScreenEffect

fun handleBaseRecipeInputScreenEffect(
  effect: RecipeInputScreenEffect,
  navigator: RecipeInputScreenBaseNavigator
) {
  when (effect) {
    is RecipeInputScreenEffect.OnSaved -> navigator.openSavedDialog()
    is RecipeInputScreenEffect.OnBack -> navigator.navigateUp()
    is RecipeInputScreenEffect.OnClose -> navigator.closeRecipeInput(recipeId = effect.recipeId)
    else -> Unit
  }
}
