package io.chefbook.features.category.ui.input.mvi

import io.chefbook.libs.mvi.MviSideEffect
import io.chefbook.sdk.collection.api.external.domain.entities.Collection

internal sealed class CategoryInputScreenEffect : MviSideEffect {
  data object Cancel : CategoryInputScreenEffect()
  data object OpenDeleteConfirmation : CategoryInputScreenEffect()

  data class CategoryCreated(val collection: Collection) : CategoryInputScreenEffect()
  data class CategoryUpdated(val collection: Collection) : CategoryInputScreenEffect()
  data class CategoryDeleted(val categoryId: String) : CategoryInputScreenEffect()
}
