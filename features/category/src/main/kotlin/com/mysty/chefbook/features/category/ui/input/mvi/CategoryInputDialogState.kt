package com.mysty.chefbook.features.category.ui.input.mvi

import com.mysty.chefbook.api.category.domain.entities.CategoryInput
import com.mysty.chefbook.core.android.mvi.MviState

internal data class CategoryInputDialogState(
  val input: CategoryInput = CategoryInput(),
  val isEditing: Boolean = false,
) : MviState
