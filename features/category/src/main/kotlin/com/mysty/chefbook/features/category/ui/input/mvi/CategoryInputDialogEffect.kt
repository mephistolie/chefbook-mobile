package com.mysty.chefbook.features.category.ui.input.mvi

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.core.android.mvi.MviSideEffect

internal sealed class CategoryInputDialogEffect : MviSideEffect {
  object Cancel : CategoryInputDialogEffect()
  object OpenDeleteConfirmation : CategoryInputDialogEffect()

  data class CategoryCreated(val category: Category) : CategoryInputDialogEffect()
  data class CategoryUpdated(val category: Category) : CategoryInputDialogEffect()
  data class CategoryDeleted(val categoryId: String) : CategoryInputDialogEffect()
}
