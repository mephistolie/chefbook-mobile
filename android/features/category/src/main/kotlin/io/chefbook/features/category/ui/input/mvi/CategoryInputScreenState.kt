package io.chefbook.features.category.ui.input.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput

internal data class CategoryInputScreenState(
  val input: CollectionInput = CollectionInput.new(),
  val isEditing: Boolean = false,
  val isDeleting: Boolean = false,
  val isSaving: Boolean = false,
) : MviState
