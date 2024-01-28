package io.chefbook.sdk.category.impl.data.sources

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.category.api.external.domain.entities.Category

internal interface CategorySource {
  suspend fun getCategories(): Result<List<Category>>
  suspend fun getCategory(categoryId: String): Result<Category>
  suspend fun deleteCategory(categoryId: String): EmptyResult
}
