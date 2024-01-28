package io.chefbook.features.recipe.control.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed class RecipeControlScreenEffect : MviSideEffect {
  data object OpenRemoveFromRecipeBookConfirmDialog : RecipeControlScreenEffect()
  data object OpenCategoriesSelectionPage : RecipeControlScreenEffect()
  data class EditRecipe(val recipeId: String) : RecipeControlScreenEffect()
  data object OpenDeleteRecipeConfirmDialog : RecipeControlScreenEffect()

  data class ShowToast(val messageId: Int) : RecipeControlScreenEffect()
  data object Close : RecipeControlScreenEffect()
}
