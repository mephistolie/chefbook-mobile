package io.chefbook.sdk.recipe.crud.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface DeleteRecipeUseCase {
  suspend operator fun invoke(recipeId: String): EmptyResult
}
