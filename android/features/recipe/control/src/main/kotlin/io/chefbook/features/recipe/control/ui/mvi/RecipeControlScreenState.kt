package io.chefbook.features.recipe.control.ui.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo

internal data class RecipeControlScreenState(
  val recipe: DecryptedRecipeInfo? = null,
) : MviState
