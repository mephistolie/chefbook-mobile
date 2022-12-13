package com.cactusknights.chefbook.ui.screens.category.models

sealed class CategoryScreenEffect {
    data class OnRecipeOpened(val recipeId: String): CategoryScreenEffect()
    object Back: CategoryScreenEffect()
}
