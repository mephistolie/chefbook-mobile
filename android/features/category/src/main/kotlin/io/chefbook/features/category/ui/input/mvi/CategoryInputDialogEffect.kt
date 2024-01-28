package io.chefbook.features.category.ui.input.mvi

import io.chefbook.libs.mvi.MviSideEffect
import io.chefbook.sdk.category.api.external.domain.entities.Category

internal sealed class CategoryInputDialogEffect : MviSideEffect {
  data object Cancel : CategoryInputDialogEffect()
  data object OpenDeleteConfirmation : CategoryInputDialogEffect()

  data class CategoryCreated(val category: Category) : CategoryInputDialogEffect()
  data class CategoryUpdated(val category: Category) : CategoryInputDialogEffect()
  data class CategoryDeleted(val categoryId: String) : CategoryInputDialogEffect()
}
