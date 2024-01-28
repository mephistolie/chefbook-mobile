package io.chefbook.sdk.category.api.internal.data.repositories

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

  fun observeCategories(): Flow<List<Category>?>

  suspend fun getCategories(): List<Category>

  suspend fun cacheCategories(categories: List<Category>): EmptyResult

  suspend fun createCategory(input: CategoryInput): Result<Category>

  suspend fun getCategory(categoryId: String): Result<Category>

  suspend fun updateCategory(categoryId: String, input: CategoryInput): Result<Category>

  suspend fun deleteCategory(categoryId: String): EmptyResult

  suspend fun clearLocalData(exceptProfileId: String? = null): EmptyResult
}
