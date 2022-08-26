package com.cactusknights.chefbook.ui.screens.category.models

sealed class CategoryScreenEffect {
    data class OnRecipeOpened(val recipeId: Int): CategoryScreenEffect()
    object Back: CategoryScreenEffect()
}
