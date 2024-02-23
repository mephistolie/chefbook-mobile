package io.chefbook.features.recipe.rating.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed interface RecipeRatingScreenEffect : MviSideEffect {

  data object Close : RecipeRatingScreenEffect
}
