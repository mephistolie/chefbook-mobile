package io.chefbook.features.recipebook.favourite.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed class FavouriteRecipesScreenEffect : MviSideEffect {
  data class OnRecipeOpened(val recipeId: String) : FavouriteRecipesScreenEffect()
  data object Back : FavouriteRecipesScreenEffect()
}
