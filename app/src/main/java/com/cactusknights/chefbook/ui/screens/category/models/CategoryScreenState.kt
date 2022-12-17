package com.cactusknights.chefbook.ui.screens.category.models

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.category.domain.entities.CategoryInput
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo

data class CategoryScreenState(
    val category: Category? = null,
    val recipes: List<RecipeInfo> = emptyList(),
    val cachedCategoryInput: CategoryInput? = null,

    val isEditCategoryDialogVisible: Boolean = false,
    val isDeleteCategoryDialogVisible: Boolean = false,
)
