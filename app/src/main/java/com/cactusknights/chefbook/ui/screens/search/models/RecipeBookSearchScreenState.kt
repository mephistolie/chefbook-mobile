package com.cactusknights.chefbook.ui.screens.search.models

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.core.constants.Strings

data class RecipeBookSearchScreenState(
    val query: String = Strings.EMPTY,
    val categories: List<Category> = emptyList(),
    val recipes: List<RecipeInfo> = emptyList(),
    val isLoading: Boolean = false,
)
