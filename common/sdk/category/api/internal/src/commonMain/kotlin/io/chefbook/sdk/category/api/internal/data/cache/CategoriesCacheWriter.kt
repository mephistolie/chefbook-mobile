package io.chefbook.sdk.category.api.internal.data.cache

import io.chefbook.sdk.category.api.external.domain.entities.Category

interface CategoriesCacheWriter {
  suspend fun setCategories(categories: List<Category>)
  suspend fun addCategory(category: Category)
  suspend fun updateCategory(category: Category)
  suspend fun removeCategory(categoryId: String)

  suspend fun clear()
}
