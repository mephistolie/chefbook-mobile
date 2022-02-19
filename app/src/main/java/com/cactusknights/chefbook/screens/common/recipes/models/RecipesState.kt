package com.cactusknights.chefbook.screens.common.recipes.models

import com.cactusknights.chefbook.models.RecipeInfo
import javax.crypto.SecretKey

sealed class RecipesState {
    object Loading : RecipesState()
    data class RecipesUpdated(val recipes: List<RecipeInfo>, val keys : Map<String, SecretKey>? = null) : RecipesState()
    data class SearchResult(val result: List<RecipeInfo>, val keys : Map<String, SecretKey>? = null) : RecipesState()
}