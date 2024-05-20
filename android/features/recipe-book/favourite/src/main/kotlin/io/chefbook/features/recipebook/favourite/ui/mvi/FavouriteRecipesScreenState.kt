package io.chefbook.features.recipebook.favourite.ui.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo

internal data class FavouriteRecipesScreenState(
  val recipes: List<DecryptedRecipeInfo>? = null,
) : MviState
