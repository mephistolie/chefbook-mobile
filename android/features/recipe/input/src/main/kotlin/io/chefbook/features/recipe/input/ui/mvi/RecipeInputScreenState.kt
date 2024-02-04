package io.chefbook.features.recipe.input.ui.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput

data class RecipeInputScreenState(
  val input: RecipeInput = RecipeInput.new(),
  val isEditing: Boolean = false,
  val isLoading: Boolean = false,
) : MviState
