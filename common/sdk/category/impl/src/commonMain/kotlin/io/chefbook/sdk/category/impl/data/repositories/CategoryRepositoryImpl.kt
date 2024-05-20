package io.chefbook.sdk.category.impl.data.repositories

import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.coroutines.CoroutineScopes
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.onSuccess
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput
import io.chefbook.sdk.category.api.external.domain.entities.toCategory
import io.chefbook.sdk.category.api.internal.data.cache.CategoriesCache
import io.chefbook.sdk.category.api.internal.data.repositories.CategoryRepository
import io.chefbook.sdk.category.impl.data.sources.local.LocalCategorySource
import io.chefbook.sdk.category.impl.data.sources.remote.RemoteCategorySource
import io.chefbook.sdk.core.api.internal.data.repositories.DataSourcesRepository
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class CategoryRepositoryImpl(
  private val localSource: LocalCategorySource,
  private val remoteSource: RemoteCategorySource,

  private val profileRepository: ProfileRepository,
  private val sources: DataSourcesRepository,
  private val cache: CategoriesCache,
  scopes: CoroutineScopes,
  private val dispatchers: AppDispatchers,
) : CategoryRepository {

  private val loadInitCategoriesJob = scopes.repository.launch { loadCachedCategories() }

  override fun observeCategories(): StateFlow<List<Category>?> = cache.observeCategories()

  override suspend fun getCategories(): List<Category> = withContext(dispatchers.io) {
    loadInitCategoriesJob.join()
    return@withContext cache.getCategories()
  }

  private suspend fun loadCachedCategories() =
    localSource.getCategories().onSuccess(cache::setCategories)

  override suspend fun cacheCategories(categories: List<Category>) = withContext(dispatchers.io) {
    loadInitCategoriesJob.join()

    cache.setCategories(categories)
    pullChanges(localSource.getCategories().getOrDefault(emptyList()), categories)

    return@withContext successResult
  }

  private suspend fun pullChanges(old: List<Category>, new: List<Category>) {
    for (newCategory in new) {
      val localCategory = old.find { it.id == newCategory.id }
      if (localCategory == null || localCategory != newCategory) {
        localSource.insertCategory(newCategory, ownerId = profileRepository.getProfileId())
      }
    }

    val newIds = new.map { it.id }
    val deletedCategories = old.filter { it.id !in newIds }
    for (category in deletedCategories) {
      localSource.deleteCategory(category.id)
    }
  }

  override suspend fun createCategory(input: CategoryInput): Result<Category> {
    val result = if (sources.isRemoteSourceEnabled()) {
      remoteSource.createCategory(input).map { id ->
        input.toCategory(id = id).also { category ->
          localSource.insertCategory(category, ownerId = profileRepository.getProfileId())
        }
      }
    } else {
      val category = input.toCategory()
      localSource.insertCategory(category, ownerId = profileRepository.getProfileId()).map { category }
    }

    return result.onSuccess(cache::addCategory)
  }

  override suspend fun getCategory(categoryId: String): Result<Category> {
    var result = localSource.getCategory(categoryId)

    if (result.isFailure && sources.isRemoteSourceAvailable()) {
      result = remoteSource.getCategory(categoryId)
    }

    return result
  }

  override suspend fun updateCategory(
    categoryId: String,
    input: CategoryInput
  ): Result<Category> {
    if (sources.isRemoteSourceEnabled()) {
      remoteSource.updateCategory(categoryId, input).onFailure { return Result.failure(it) }
    }

    val updatedCategory = input.toCategory(id = categoryId)
    localSource.insertCategory(updatedCategory, ownerId = profileRepository.getProfileId())
    cache.updateCategory(updatedCategory)

    return Result.success(updatedCategory)
  }

  override suspend fun deleteCategory(categoryId: String): EmptyResult {
    if (sources.isRemoteSourceEnabled()) {
      remoteSource.deleteCategory(categoryId).onFailure { return Result.failure(it) }
    }

    localSource.deleteCategory(categoryId)
    cache.removeCategory(categoryId)

    return successResult
  }

  override suspend fun clearLocalData(exceptProfileId: String?): EmptyResult {
    val result = localSource.clearCategories(exceptProfileId)
    cache.clear()

    return result
  }
}
