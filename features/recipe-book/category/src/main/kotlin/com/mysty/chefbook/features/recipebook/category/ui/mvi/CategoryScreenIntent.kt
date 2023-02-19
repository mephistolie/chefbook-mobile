package com.mysty.chefbook.features.recipebook.category.ui.mvi

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.core.android.mvi.MviIntent

internal sealed class CategoryScreenIntent : MviIntent {
    data class OpenRecipeScreen(val recipeId: String) : CategoryScreenIntent()
    object OpenCategoryInputDialog : CategoryScreenIntent()
    data class OnCategoryUpdated(val category: Category) : CategoryScreenIntent()
    object Back : CategoryScreenIntent()
}
