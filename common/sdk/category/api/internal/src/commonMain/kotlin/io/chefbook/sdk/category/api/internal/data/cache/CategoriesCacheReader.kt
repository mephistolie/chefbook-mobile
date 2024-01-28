package io.chefbook.sdk.category.api.internal.data.cache

import io.chefbook.sdk.category.api.external.domain.entities.Category
import kotlinx.coroutines.flow.StateFlow

interface CategoriesCacheReader {
  fun observeCategories(): StateFlow<List<Category>?>
  suspend fun getCategories(): List<Category>
}