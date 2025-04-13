package io.chefbook.features.recipebook.category.ui.mvi

import io.chefbook.libs.mvi.MviIntent
import io.chefbook.sdk.collection.api.external.domain.entities.Collection

internal sealed class CategoryScreenIntent : MviIntent {
    data class OpenRecipeScreen(val recipeId: String) : CategoryScreenIntent()
    data object OpenCategoryInputDialog : CategoryScreenIntent()
    data class OnCategoryUpdated(val collection: Collection) : CategoryScreenIntent()
    data object Back : CategoryScreenIntent()
}
