package io.chefbook.features.recipe.rating.ui.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed interface RecipeRatingScreenIntent : MviIntent {

  data class ScoreClicked(val score: Int) : RecipeRatingScreenIntent
}
