package io.chefbook.sdk.category.api.external.domain.usecases

import io.chefbook.sdk.category.api.external.domain.entities.Category
import kotlinx.coroutines.flow.Flow

fun interface ObserveCategoriesUseCase {
  operator fun invoke(): Flow<List<Category>?>
}
