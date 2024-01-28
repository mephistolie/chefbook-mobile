package io.chefbook.features.recipebook.favourite.ui

import androidx.compose.runtime.Composable
import io.chefbook.features.recipebook.favourite.ui.components.FavouriteRecipesToolbarContent
import io.chefbook.features.recipebook.favourite.ui.mvi.FavouriteRecipesScreenIntent
import io.chefbook.features.recipebook.favourite.ui.mvi.FavouriteRecipesScreenState
import io.chefbook.ui.common.screens.recipe.RecipesScreen

@Composable
internal fun FavouriteRecipesScreenContent(
  state: FavouriteRecipesScreenState,
  onIntent: (FavouriteRecipesScreenIntent) -> Unit,
) {
  RecipesScreen(
    recipes = state.recipes,
    onRecipeClick = { recipeId -> onIntent(FavouriteRecipesScreenIntent.OpenRecipeScreen(recipeId = recipeId)) },
    onBack = { onIntent(FavouriteRecipesScreenIntent.Back) },
    toolbarContent = { FavouriteRecipesToolbarContent() }
  )
}
