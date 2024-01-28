package io.chefbook.sdk.recipe.book.api.external.domain.usecases

import io.chefbook.sdk.recipe.book.api.external.domain.entities.RecipeBook

interface GetRecipeBookUseCase {
  suspend operator fun invoke(): RecipeBook
}
