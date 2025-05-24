package io.chefbook.features.category.ui.input.mvi

import io.chefbook.libs.mvi.MviSideEffect
import io.chefbook.sdk.collection.api.external.domain.entities.Collection

internal sealed class CollectionInputScreenEffect : MviSideEffect {
  data object Cancel : CollectionInputScreenEffect()
  data object OpenDeleteConfirmation : CollectionInputScreenEffect()

  data class CollectionCreated(val collection: Collection) : CollectionInputScreenEffect()
  data class CollectionUpdated(val collection: Collection) : CollectionInputScreenEffect()
  data class CollectionDeleted(val categoryId: String) : CollectionInputScreenEffect()
}
