package io.chefbook.sdk.recipe.book.impl.domain.usecases

import io.chefbook.sdk.recipe.book.api.external.domain.usecases.GetRecipeBookUseCase
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.RecipeBookRepository

internal class GetRecipeBookUseCaseImpl(
  private val recipesRepository: RecipeBookRepository,
) : GetRecipeBookUseCase {

  override suspend operator fun invoke() = recipesRepository.getRecipeBook()
}
