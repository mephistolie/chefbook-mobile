package io.chefbook.features.recipe.control.ui.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed class RecipeControlScreenIntent : MviIntent {
  data object ChangeFavouriteStatus : RecipeControlScreenIntent()
  data object ChangeSavedStatus : RecipeControlScreenIntent()
  data object ChangeCategories : RecipeControlScreenIntent()
  data object EditRecipe : RecipeControlScreenIntent()
  data object DeleteRecipe : RecipeControlScreenIntent()

  data object OpenRemoveFromRecipeBookDialog : RecipeControlScreenIntent()
  data object OpenDeleteDialog : RecipeControlScreenIntent()

  data object Close : RecipeControlScreenIntent()
}
