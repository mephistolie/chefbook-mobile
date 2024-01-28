package io.chefbook.sdk.recipe.crud.api.external.domain.usecases

import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import kotlinx.coroutines.flow.Flow

interface ObserveRecipeUseCase {
  suspend operator fun invoke(
    recipeId: String,
  ): Flow<Recipe?>
}
