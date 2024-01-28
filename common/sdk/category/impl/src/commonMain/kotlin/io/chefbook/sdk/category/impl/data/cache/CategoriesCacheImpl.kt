package io.chefbook.sdk.category.impl.data.cache

import io.chefbook.libs.logger.Logger
import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.category.api.internal.data.cache.CategoriesCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class CategoriesCacheImpl : CategoriesCache {

  private val cachedList = MutableStateFlow<List<Category>?>(null)

  override fun observeCategories(): StateFlow<List<Category>?> = cachedList.asStateFlow()

  override suspend fun getCategories(): List<Category> = cachedList.value.orEmpty()

  override suspend fun setCategories(categories: List<Category>) {
    cachedList.emit(categories)
    Logger.d("Categories set: ${categories.map { it.id }}")
  }

  override suspend fun addCategory(category: Category) {
    cachedList.update { categories ->
      categories?.filter { it.id != category.id }.orEmpty().plus(category)
    }
    Logger.d("Category added: ${category.id}")
  }

  override suspend fun updateCategory(category: Category) {
    cachedList.update { categories ->
      categories?.map { if (it.id != category.id) it else category }
    }
    Logger.d("Category updated: ${category.id}")
  }

  override suspend fun removeCategory(categoryId: String) {
    cachedList.update { categories -> categories?.filter { it.id != categoryId } }
    Logger.d("Category removed: $categoryId")
  }

  override suspend fun clear() = cachedList.emit(null)
}
