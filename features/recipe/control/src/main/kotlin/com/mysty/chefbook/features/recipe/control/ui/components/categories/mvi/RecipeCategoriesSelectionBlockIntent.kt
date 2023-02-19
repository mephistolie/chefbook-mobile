package com.mysty.chefbook.features.recipe.control.ui.components.categories.mvi

import com.mysty.chefbook.core.android.mvi.MviIntent

sealed class RecipeCategoriesSelectionBlockIntent: MviIntent {
  object Cancel : RecipeCategoriesSelectionBlockIntent()
  data class ChangeSelectStatus(val category: String) : RecipeCategoriesSelectionBlockIntent()
  object ConfirmSelection : RecipeCategoriesSelectionBlockIntent()
}
