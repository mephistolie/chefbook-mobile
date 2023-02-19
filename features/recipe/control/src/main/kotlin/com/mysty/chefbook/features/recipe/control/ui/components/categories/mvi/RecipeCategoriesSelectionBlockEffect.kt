package com.mysty.chefbook.features.recipe.control.ui.components.categories.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

sealed class RecipeCategoriesSelectionBlockEffect : MviSideEffect {
  data class ShowToast(val messageId: Int) : RecipeCategoriesSelectionBlockEffect()
  object Close : RecipeCategoriesSelectionBlockEffect()
}
