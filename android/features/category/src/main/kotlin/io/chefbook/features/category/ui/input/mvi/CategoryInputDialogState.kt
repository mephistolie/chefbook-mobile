package io.chefbook.features.category.ui.input.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput

internal data class CategoryInputDialogState(
  val input: CategoryInput = CategoryInput(),
  val isEditing: Boolean = false,
) : MviState
