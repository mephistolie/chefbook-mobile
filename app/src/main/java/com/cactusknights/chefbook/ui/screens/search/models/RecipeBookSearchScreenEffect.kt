package com.cactusknights.chefbook.ui.screens.search.models

sealed class RecipeBookSearchScreenEffect {
    data class OnCategoryOpened(val categoryId: Int): RecipeBookSearchScreenEffect()
    data class OnRecipeOpened(val recipeId: Int): RecipeBookSearchScreenEffect()
    object Back: RecipeBookSearchScreenEffect()
}
