package io.chefbook.features.recipe.control.ui.components.categories.mvi

import io.chefbook.libs.mvi.MviSideEffect

sealed class RecipeCategoriesSelectionBlockEffect : MviSideEffect {
  data class ShowToast(val messageId: Int) : RecipeCategoriesSelectionBlockEffect()
  data object Close : RecipeCategoriesSelectionBlockEffect()
}
