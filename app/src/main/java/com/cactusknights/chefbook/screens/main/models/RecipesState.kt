package com.cactusknights.chefbook.screens.main.models

import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Recipe

sealed class RecipesState {
    object Loading : RecipesState()
    data class RecipesUpdated(val recipes: List<Recipe>) : RecipesState()
    data class SearchResult(val result: List<Recipe>) : RecipesState()
}