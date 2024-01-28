package io.chefbook.sdk.recipe.interaction.impl.domain.usecases

import io.chefbook.sdk.recipe.interaction.api.external.domain.usecases.SetRecipeCategoriesUseCase
import io.chefbook.sdk.recipe.interaction.impl.data.repositories.RecipeInteractionRepository

internal class SetRecipeCategoriesUseCaseImpl(
  private val repository: RecipeInteractionRepository,
) : SetRecipeCategoriesUseCase {

  override suspend fun invoke(recipeId: String, categories: List<String>) =
    repository.setRecipeCategories(recipeId, categories)
}
