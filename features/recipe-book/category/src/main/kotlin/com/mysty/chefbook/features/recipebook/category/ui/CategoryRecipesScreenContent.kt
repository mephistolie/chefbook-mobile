package com.mysty.chefbook.features.recipebook.category.ui

import androidx.compose.runtime.Composable
import com.mysty.chefbook.features.recipebook.category.ui.components.CategoryRecipesToolbarContent
import com.mysty.chefbook.features.recipebook.category.ui.mvi.CategoryScreenIntent
import com.mysty.chefbook.features.recipebook.category.ui.mvi.CategoryScreenState
import com.mysty.chefbook.ui.common.screens.recipe.RecipesScreen

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
        toolbarContent = { CategoryRecipesToolbarContent(
            category = state.category,
            recipesCount = state.recipes.size,
        ) }
    )
}
