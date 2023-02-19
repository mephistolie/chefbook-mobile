package com.mysty.chefbook.features.recipebook.favourite.ui

import androidx.compose.runtime.Composable
import com.mysty.chefbook.features.recipebook.favourite.ui.components.FavouriteRecipesToolbarContent
import com.mysty.chefbook.features.recipebook.favourite.ui.mvi.FavouriteRecipesScreenIntent
import com.mysty.chefbook.features.recipebook.favourite.ui.mvi.FavouriteRecipesScreenState
import com.mysty.chefbook.ui.common.screens.recipe.RecipesScreen

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
