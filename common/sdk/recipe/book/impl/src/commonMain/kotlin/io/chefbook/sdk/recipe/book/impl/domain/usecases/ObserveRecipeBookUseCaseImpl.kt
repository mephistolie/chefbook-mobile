package io.chefbook.sdk.recipe.book.impl.domain.usecases

import io.chefbook.sdk.recipe.book.api.external.domain.usecases.ObserveRecipeBookUseCase
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.RecipeBookRepository

internal class ObserveRecipeBookUseCaseImpl(
  private val recipeRepository: RecipeBookRepository,
) : ObserveRecipeBookUseCase {

  override operator fun invoke() = recipeRepository.observeRecipeBook()
}
