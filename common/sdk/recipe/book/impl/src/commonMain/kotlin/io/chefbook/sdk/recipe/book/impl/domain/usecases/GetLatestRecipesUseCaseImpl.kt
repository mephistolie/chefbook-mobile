package io.chefbook.sdk.recipe.book.impl.domain.usecases

import io.chefbook.sdk.recipe.book.api.external.domain.usecases.GetLatestRecipesUseCase
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.LatestRecipesRepository


internal class GetLatestRecipesUseCaseImpl(
  private val recipeRepository: LatestRecipesRepository,
) : GetLatestRecipesUseCase {

  override suspend operator fun invoke() = recipeRepository.getLatestRecipes()
}
