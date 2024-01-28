package io.chefbook.features.recipe.info.ui.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed class RecipeScreenIntent : MviIntent {
  data object ReloadRecipe : RecipeScreenIntent()
  data object ChangeLikeStatus : RecipeScreenIntent()
  data object AddToRecipeBook : RecipeScreenIntent()
  data object OpenRecipeMenu : RecipeScreenIntent()
  data object OpenRecipeDetails : RecipeScreenIntent()

  data class ChangeIngredientSelectedStatus(val ingredientId: String) : RecipeScreenIntent()
  data class ChangeServings(val offset: Int) : RecipeScreenIntent()
  data object AddSelectedIngredientsToShoppingList : RecipeScreenIntent()

  data object OpenShareDialog : RecipeScreenIntent()
  data class OpenPicturesViewer(val selectedPicture: String = "") : RecipeScreenIntent()

  data object Close : RecipeScreenIntent()
}
