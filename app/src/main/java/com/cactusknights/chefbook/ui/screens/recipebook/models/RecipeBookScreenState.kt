package com.cactusknights.chefbook.ui.screens.recipebook.models

import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.encryption.EncryptedVaultState
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo

data class RecipeBookScreenState(
    val allRecipes: List<RecipeInfo>? = null,
    val latestRecipes: List<RecipeInfo>? = null,
    val categories: List<Category>? = null,
    val encryption: EncryptedVaultState? = null,
    val isCategoriesExpanded: Boolean = false,

    val isNewCategoryDialogVisible: Boolean = false,
)
