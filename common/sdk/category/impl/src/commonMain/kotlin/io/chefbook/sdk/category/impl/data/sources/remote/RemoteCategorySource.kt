package io.chefbook.sdk.category.impl.data.sources.remote

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput
import io.chefbook.sdk.category.impl.data.sources.CategorySource

internal interface RemoteCategorySource : CategorySource {
  suspend fun createCategory(input: CategoryInput): Result<String>
  suspend fun updateCategory(categoryId: String, input: CategoryInput): EmptyResult
}
