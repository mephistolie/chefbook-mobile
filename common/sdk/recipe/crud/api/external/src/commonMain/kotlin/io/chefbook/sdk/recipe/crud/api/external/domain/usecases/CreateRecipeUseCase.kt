package io.chefbook.sdk.recipe.crud.api.external.domain.usecases

import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput

interface CreateRecipeUseCase {
  suspend operator fun invoke(input: RecipeInput): Result<String>
}
