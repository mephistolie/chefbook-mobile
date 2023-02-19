package com.mysty.chefbook.features.recipebook.category.ui.mvi

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.category.domain.entities.CategoryInput
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.core.android.mvi.MviState

internal data class CategoryScreenState(
    val category: Category? = null,
    val recipes: List<RecipeInfo> = emptyList(),
    val cachedCategoryInput: CategoryInput? = null,
) : MviState
