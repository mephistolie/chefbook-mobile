package io.chefbook.sdk.category.impl.data.sources.local

import io.chefbook.sdk.database.api.internal.DatabaseDataSource
import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.category.impl.data.sources.local.sql.dto.toDto
import io.chefbook.sdk.category.impl.data.sources.local.sql.dto.toEntity
import io.chefbook.sdk.database.api.internal.ChefBookDatabase
import io.chefbook.sdk.database.api.internal.Category as CategorySql

internal class LocalCategorySourceImpl(
  database: ChefBookDatabase,
) : DatabaseDataSource(), LocalCategorySource {

  private val queries = database.categoryQueries

  override suspend fun getCategories(): Result<List<Category>> = safeQueryResult {
    queries.selectAll().executeAsList().map(CategorySql::toEntity)
  }

  override suspend fun getCategory(categoryId: String): Result<Category> = safeQueryResult {
    queries.select(categoryId).executeAsOne().toEntity()
  }

  override suspend fun insertCategory(category: Category, ownerId: String) = safeQueryResult {
    queries.insert(category.toDto(ownerId))
  }

  override suspend fun deleteCategory(categoryId: String) = safeQueryResult {
    queries.delete(categoryId)
  }

  override suspend fun clearCategories(exceptProfileId: String?) = safeQueryResult {
    if (exceptProfileId != null) {
      queries.clearExcept(exceptProfileId)
    } else {
      queries.clear()
    }
  }
}
