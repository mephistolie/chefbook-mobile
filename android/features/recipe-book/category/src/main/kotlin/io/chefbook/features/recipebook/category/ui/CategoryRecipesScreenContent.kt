package io.chefbook.features.recipebook.category.ui

import androidx.compose.runtime.Composable
import io.chefbook.features.recipebook.category.ui.components.CategoryRecipesToolbarContent
import io.chefbook.features.recipebook.category.ui.mvi.CategoryScreenIntent
import io.chefbook.features.recipebook.category.ui.mvi.CategoryScreenState
import io.chefbook.ui.common.screens.recipe.RecipesScreen

@Composable
internal fun CategoryScreenContent(
  state: CategoryScreenState,
  onIntent: (CategoryScreenIntent) -> Unit,
) {
  RecipesScreen(
    recipes = state.recipes,
    onRecipeClick = { recipeId -> onIntent(CategoryScreenIntent.OpenRecipeScreen(recipeId = recipeId)) },
    onBack = { onIntent(CategoryScreenIntent.Back) },
    onToolbarContentClick = { onIntent(CategoryScreenIntent.OpenCategoryInputDialog) },
    toolbarContent = {
      CategoryRecipesToolbarContent(
        category = state.category,
        recipesCount = state.recipes.size,
      )
    }
  )
}
