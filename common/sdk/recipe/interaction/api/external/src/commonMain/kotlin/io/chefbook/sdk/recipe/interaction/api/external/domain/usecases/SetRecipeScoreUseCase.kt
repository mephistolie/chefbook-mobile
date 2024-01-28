package io.chefbook.sdk.recipe.interaction.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

fun interface SetRecipeScoreUseCase {
  suspend operator fun invoke(recipeId: String, score: Int?): EmptyResult
}
