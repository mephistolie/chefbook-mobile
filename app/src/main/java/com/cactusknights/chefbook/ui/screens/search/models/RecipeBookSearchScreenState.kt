package com.cactusknights.chefbook.ui.screens.search.models

import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo

data class RecipeBookSearchScreenState(
    val query: String = "",
    val categories: List<Category> = emptyList(),
    val recipes: List<RecipeInfo> = emptyList(),
    val isLoading: Boolean = false,
)
