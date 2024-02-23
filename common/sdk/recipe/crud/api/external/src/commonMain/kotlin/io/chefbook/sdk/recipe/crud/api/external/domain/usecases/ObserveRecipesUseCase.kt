package io.chefbook.sdk.recipe.crud.api.external.domain.usecases

import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import kotlinx.coroutines.flow.Flow

fun interface ObserveRecipesUseCase {

  operator fun invoke(): Flow<Map<String, Recipe>>
}
