package io.chefbook.sdk.recipe.crud.impl.domain

import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.ObserveRecipesUseCase
import io.chefbook.sdk.recipe.crud.impl.data.repositories.RecipeCrudRepository

internal class ObserveRecipesUseCaseImpl(
  private val recipeRepository: RecipeCrudRepository,
) : ObserveRecipesUseCase {

  override operator fun invoke() = recipeRepository.observeRecipes()
}
