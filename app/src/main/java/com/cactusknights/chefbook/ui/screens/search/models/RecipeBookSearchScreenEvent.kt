package com.cactusknights.chefbook.ui.screens.search.models

sealed class RecipeBookSearchScreenEvent {
    data class Search(val query: String): RecipeBookSearchScreenEvent()
    data class OpenCategoryScreen(val categoryId: String) : RecipeBookSearchScreenEvent()
    data class OpenRecipeScreen(val recipeId: String) : RecipeBookSearchScreenEvent()
    object Back : RecipeBookSearchScreenEvent()
}
