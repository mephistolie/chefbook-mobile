package com.cactusknights.chefbook.ui.screens.search.models

sealed class RecipeBookSearchScreenEffect {
    data class OnCategoryOpened(val categoryId: String): RecipeBookSearchScreenEffect()
    data class OnRecipeOpened(val recipeId: String): RecipeBookSearchScreenEffect()
    object Back: RecipeBookSearchScreenEffect()
}
