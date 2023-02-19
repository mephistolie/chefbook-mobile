package com.mysty.chefbook.features.recipebook.search.ui.mvi

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.core.android.mvi.MviState
import com.mysty.chefbook.core.constants.Strings

internal data class RecipeBookSearchScreenState(
    val query: String = Strings.EMPTY,
    val categories: List<Category> = emptyList(),
    val recipes: List<RecipeInfo> = emptyList(),
    val isLoading: Boolean = false,
) : MviState
