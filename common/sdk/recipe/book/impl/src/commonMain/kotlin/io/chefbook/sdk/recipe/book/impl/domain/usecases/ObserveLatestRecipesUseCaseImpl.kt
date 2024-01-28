package io.chefbook.sdk.recipe.book.impl.domain.usecases

import io.chefbook.sdk.recipe.book.api.external.domain.usecases.ObserveLatestRecipesUseCase
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.LatestRecipesRepository

internal class ObserveLatestRecipesUseCaseImpl(
  private val recipeRepository: LatestRecipesRepository,
) : ObserveLatestRecipesUseCase {

  override operator fun invoke() = recipeRepository.observeLatestRecipes()
}
