package io.chefbook.sdk.recipe.book.api.external.domain.usecases

import io.chefbook.sdk.recipe.book.api.external.domain.entities.LatestRecipeInfo
import kotlinx.coroutines.flow.Flow

fun interface ObserveLatestRecipesUseCase {
  operator fun invoke(): Flow<List<LatestRecipeInfo>>
}
