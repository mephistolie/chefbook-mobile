package io.chefbook.sdk.recipe.crud.api.external.domain.usecases

import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe

interface GetRecipeUseCase {
  suspend operator fun invoke(recipeId: String): Result<Recipe>
}
