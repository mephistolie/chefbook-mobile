package io.chefbook.features.recipe.rating.ui.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta

internal data class RecipeRatingScreenState(
  val rating: RecipeMeta.Rating = RecipeMeta.Rating(),
  val isScoreVisible: Boolean = false,
) : MviState
