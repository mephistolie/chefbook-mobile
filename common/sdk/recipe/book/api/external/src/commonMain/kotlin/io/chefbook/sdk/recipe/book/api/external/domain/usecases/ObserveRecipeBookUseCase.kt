package io.chefbook.sdk.recipe.book.api.external.domain.usecases

import io.chefbook.sdk.recipe.book.api.external.domain.entities.RecipeBook
import kotlinx.coroutines.flow.Flow

fun interface ObserveRecipeBookUseCase {
  operator fun invoke(): Flow<RecipeBook?>
}
