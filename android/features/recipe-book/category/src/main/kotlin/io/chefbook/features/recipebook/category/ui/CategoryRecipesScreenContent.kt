package io.chefbook.features.recipebook.category.ui

import androidx.compose.runtime.Composable
import io.chefbook.features.recipebook.category.ui.components.CategoryRecipesToolbarContent
import io.chefbook.features.recipebook.category.ui.mvi.CategoryScreenIntent
import io.chefbook.features.recipebook.category.ui.mvi.CategoryScreenState
import io.chefbook.features.recipebook.core.ui.screens.recipe.RecipesScreen

@Composable
internal fun CategoryScreenContent(
  state: CategoryScreenState,
  onIntent: (CategoryScreenIntent) -> Unit,
) {
  RecipesScreen(
    recipes = state.recipes,
    onRecipeClick = { recipeId -> onIntent(CategoryScreenIntent.OpenRecipeScreen(recipeId = recipeId)) },
    onBack = { onIntent(CategoryScreenIntent.Back) },
    onToolbarContentClick = {
      if (state.isEditButtonAvailable) onIntent(CategoryScreenIntent.OpenCategoryInputDialog)
                            },
    toolbarContent = {
      CategoryRecipesToolbarContent(
        name = state.name,
        emoji = state.emoji,
        isEditButtonAvailable = state.isEditButtonAvailable,
        recipesCount = state.recipes.size,
      )
    }
  )
}
