package io.chefbook.features.category.ui.input.mvi

import io.chefbook.libs.mvi.MviSideEffect
import io.chefbook.sdk.category.api.external.domain.entities.Category

internal sealed class CategoryInputScreenEffect : MviSideEffect {
  data object Cancel : CategoryInputScreenEffect()
  data object OpenDeleteConfirmation : CategoryInputScreenEffect()

  data class CategoryCreated(val category: Category) : CategoryInputScreenEffect()
  data class CategoryUpdated(val category: Category) : CategoryInputScreenEffect()
  data class CategoryDeleted(val categoryId: String) : CategoryInputScreenEffect()
}
