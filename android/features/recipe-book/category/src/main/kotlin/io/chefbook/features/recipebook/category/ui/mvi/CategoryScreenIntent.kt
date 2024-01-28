package io.chefbook.features.recipebook.category.ui.mvi

import io.chefbook.libs.mvi.MviIntent
import io.chefbook.sdk.category.api.external.domain.entities.Category

internal sealed class CategoryScreenIntent : MviIntent {
    data class OpenRecipeScreen(val recipeId: String) : CategoryScreenIntent()
    data object OpenCategoryInputDialog : CategoryScreenIntent()
    data class OnCategoryUpdated(val category: Category) : CategoryScreenIntent()
    data object Back : CategoryScreenIntent()
}
