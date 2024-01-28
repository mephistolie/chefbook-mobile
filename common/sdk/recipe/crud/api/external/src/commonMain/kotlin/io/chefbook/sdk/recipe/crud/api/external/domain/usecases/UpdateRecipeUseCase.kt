package io.chefbook.sdk.recipe.crud.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput

interface UpdateRecipeUseCase {
  suspend operator fun invoke(input: RecipeInput): EmptyResult
}
