package io.chefbook.sdk.recipe.interaction.impl.domain.usecases

import io.chefbook.sdk.recipe.interaction.api.external.domain.usecases.SetRecipeScoreUseCase
import io.chefbook.sdk.recipe.interaction.impl.data.repositories.RecipeInteractionRepository

internal class SetRecipeScoreUseCaseImpl(
  private val repository: RecipeInteractionRepository,
) : SetRecipeScoreUseCase {

  override suspend fun invoke(recipeId: String, score: Int?) =
    repository.setRecipeScore(recipeId, score)
}
