package io.chefbook.sdk.category.impl.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.category.impl.data.sources.CategorySource

internal interface LocalCategorySource : CategorySource {
  suspend fun insertCategory(category: Category, ownerId: String): EmptyResult
  suspend fun clearCategories(exceptProfileId: String? = null): EmptyResult
}
