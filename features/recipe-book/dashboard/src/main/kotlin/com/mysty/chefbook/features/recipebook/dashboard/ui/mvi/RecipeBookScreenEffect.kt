package com.mysty.chefbook.features.recipebook.dashboard.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

internal sealed class RecipeBookScreenEffect : MviSideEffect {
    object OpenRecipeCreationScreen : RecipeBookScreenEffect()
    object OpenRecipeSearchScreen : RecipeBookScreenEffect()
    object OpenFavouriteRecipesScreen : RecipeBookScreenEffect()
    object OpenCommunityRecipesScreen : RecipeBookScreenEffect()
    object OpenEncryptedVaultScreen : RecipeBookScreenEffect()
    data class OpenCategoryRecipesScreen(val categoryId: String) : RecipeBookScreenEffect()
    object OpenCategoryCreationScreen : RecipeBookScreenEffect()
    data class OpenRecipeScreen(val recipeId: String) : RecipeBookScreenEffect()
}
