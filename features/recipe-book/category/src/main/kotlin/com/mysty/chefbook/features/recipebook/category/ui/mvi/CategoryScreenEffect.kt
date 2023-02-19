package com.mysty.chefbook.features.recipebook.category.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

internal sealed class CategoryScreenEffect : MviSideEffect {
    data class OpenRecipeScreen(val recipeId: String) : CategoryScreenEffect()
    data class OpenCategoryInputDialog(val categoryId: String) : CategoryScreenEffect()
    object Back : CategoryScreenEffect()
}
