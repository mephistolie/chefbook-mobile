package com.cactusknights.chefbook.ui.screens.category.models

import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.category.CategoryInput
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo

data class CategoryScreenState(
    val category: Category? = null,
    val recipes: List<RecipeInfo> = emptyList(),
    val cachedCategoryInput: CategoryInput? = null,

    val isEditCategoryDialogVisible: Boolean = false,
    val isDeleteCategoryDialogVisible: Boolean = false,
)
