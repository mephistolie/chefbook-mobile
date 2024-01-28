package io.chefbook.features.recipe.control.ui.components.categories.mvi

import io.chefbook.libs.mvi.MviIntent

sealed class RecipeCategoriesSelectionBlockIntent : MviIntent {
  data object Cancel : RecipeCategoriesSelectionBlockIntent()
  data class ChangeSelectStatus(val category: String) : RecipeCategoriesSelectionBlockIntent()
  data object ConfirmSelection : RecipeCategoriesSelectionBlockIntent()
}
