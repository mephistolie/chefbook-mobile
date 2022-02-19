package com.cactusknights.chefbook.screens.common.recipes.models

sealed class RecipesEvent {
    object ListenRecipes : RecipesEvent()
    data class SearchRecipe(val query: String) : RecipesEvent()
}