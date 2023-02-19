package com.mysty.chefbook.features.recipebook.dashboard.ui.mvi

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.encryption.domain.entities.EncryptedVaultState
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.core.android.mvi.MviState

internal data class RecipeBookScreenState(
    val allRecipes: List<RecipeInfo>? = null,
    val latestRecipes: List<RecipeInfo>? = null,
    val categories: List<Category>? = null,
    val encryption: EncryptedVaultState? = null,
    val isCategoriesExpanded: Boolean = false,
) : MviState
