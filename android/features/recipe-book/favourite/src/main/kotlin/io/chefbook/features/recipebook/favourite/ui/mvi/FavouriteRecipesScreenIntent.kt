package io.chefbook.features.recipebook.favourite.ui.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed class FavouriteRecipesScreenIntent : MviIntent {
  data class OpenRecipeScreen(val recipeId: String) : FavouriteRecipesScreenIntent()
  data object Back : FavouriteRecipesScreenIntent()
}
