package com.cactusknights.chefbook.ui.screens.search.models

sealed class RecipeBookSearchScreenEvent {
    data class Search(val query: String): RecipeBookSearchScreenEvent()
    data class OpenCategoryScreen(val categoryId: Int) : RecipeBookSearchScreenEvent()
    data class OpenRecipeScreen(val recipeId: Int) : RecipeBookSearchScreenEvent()
    object Back : RecipeBookSearchScreenEvent()
}
