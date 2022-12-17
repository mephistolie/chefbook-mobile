package com.cactusknights.chefbook.ui.screens.recipebook.models

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.encryption.domain.entities.EncryptedVaultState
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo

data class RecipeBookScreenState(
    val allRecipes: List<RecipeInfo>? = null,
    val latestRecipes: List<RecipeInfo>? = null,
    val categories: List<Category>? = null,
    val encryption: EncryptedVaultState? = null,
    val isCategoriesExpanded: Boolean = false,

    val isNewCategoryDialogVisible: Boolean = false,
)
