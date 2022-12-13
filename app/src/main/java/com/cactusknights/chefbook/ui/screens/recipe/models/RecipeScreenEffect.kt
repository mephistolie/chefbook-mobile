package com.cactusknights.chefbook.ui.screens.recipe.models

sealed class RecipeScreenEffect {
    object EditRecipe : RecipeScreenEffect()
    data class Toast(val messageId: Int) : RecipeScreenEffect()
    data class ScreenClosed(val messageId: Int? = null) : RecipeScreenEffect()
    class CategoryScreenOpened(val categoryId: String) : RecipeScreenEffect()
}