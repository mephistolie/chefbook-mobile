package io.chefbook.sdk.recipe.interaction.impl.domain.usecases

import io.chefbook.sdk.recipe.interaction.api.external.domain.usecases.SetRecipeFavouriteStatusUseCase
import io.chefbook.sdk.recipe.interaction.impl.data.repositories.RecipeInteractionRepository

internal class SetRecipeFavouriteStatusUseCaseImpl(
  private val repository: RecipeInteractionRepository,
) : SetRecipeFavouriteStatusUseCase {

  override suspend fun invoke(recipeId: String, favourite: Boolean) =
    repository.setRecipeFavouriteStatus(recipeId, favourite)
}
