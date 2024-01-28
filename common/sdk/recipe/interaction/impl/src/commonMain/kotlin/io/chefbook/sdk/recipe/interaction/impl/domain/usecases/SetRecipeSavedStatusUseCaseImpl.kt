package io.chefbook.sdk.recipe.interaction.impl.domain.usecases

import io.chefbook.sdk.recipe.interaction.api.external.domain.usecases.SetRecipeSavedStatusUseCase
import io.chefbook.sdk.recipe.interaction.impl.data.repositories.RecipeInteractionRepository

internal class SetRecipeSavedStatusUseCaseImpl(
  private val repository: RecipeInteractionRepository,
) : SetRecipeSavedStatusUseCase {

  override suspend fun invoke(recipeId: String, saved: Boolean) =
    repository.setRecipeSavedStatus(recipeId, saved)
}
