package io.chefbook.sdk.collection.impl.data.repositories

import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.guard
import io.chefbook.libs.utils.result.onSuccess
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput
import io.chefbook.sdk.collection.api.external.domain.entities.Contributor
import io.chefbook.sdk.collection.api.external.domain.entities.toCollection
import io.chefbook.sdk.collection.impl.data.cache.CollectionsCache
import io.chefbook.sdk.collection.api.internal.data.repositories.CollectionRepository
import io.chefbook.sdk.collection.impl.data.sources.local.LocalCollectionSource
import io.chefbook.sdk.collection.impl.data.sources.remote.RemoteCollectionSource
import io.chefbook.sdk.core.api.internal.data.repositories.DataSourcesRepository
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class CollectionRepositoryImpl(
  private val localSource: LocalCollectionSource,
  private val remoteSource: RemoteCollectionSource,
  private val profileRepository: ProfileRepository,
  private val sources: DataSourcesRepository,
  private val cache: CollectionsCache,
  private val dispatchers: AppDispatchers,
  profileScope: CoroutineScope,
) : CollectionRepository {

  private val initCollectionsJob: Job =
    profileScope.launch { loadCachedCategories() }

  override fun observeCollections(): StateFlow<List<Collection>?> =
    cache.observeCollections()

  override suspend fun getCollections(): List<Collection> =
    withContext(dispatchers.io) {
      initCollectionsJob.join()
      return@withContext cache.getCollections()
    }

  private suspend fun loadCachedCategories(): Result<List<Collection>> =
    localSource.getCollections().onSuccess(cache::setCollections)

  override suspend fun cacheCollections(
    categories: List<Collection>,
  ) = withContext(dispatchers.io) {
    initCollectionsJob.join()

    cache.setCollections(categories)
    pullChanges(
      old = localSource.getCollections().getOrDefault(emptyList()),
      new = categories,
    )
  }

  private suspend fun pullChanges(
    old: List<Collection>,
    new: List<Collection>,
  ) {
    for (newCollection in new) {
      val localCategory = old.find { it.id == newCollection.id }
      if (localCategory == null || localCategory != newCollection) {
        localSource.insertCollection(newCollection)
      }
    }

    val newIds = new.map(Collection::id)
    val deletedCategories = old.filter { it.id !in newIds }
    for (category in deletedCategories) {
      localSource.deleteCollection(category.id)
    }
  }

  override suspend fun createCollection(
    input: CollectionInput,
  ): Result<String> {
    var id = input.id

    if (sources.isRemoteSourceEnabled()) {
      val result = remoteSource.createCollection(input)
      id = result.getOrNull() ?: return result
    }

    val profile = profileRepository.getProfile().getOrNull()
      ?: return Result.failure(Exception())
    val contributor = Contributor(
      profile = profile.info,
      role = Contributor.Role.OWNER,
    )

    val collection = input.toCollection(
      id = id,
      contributors = listOf(contributor),
    )

    return localSource.insertCollection(collection)
      .onSuccess { cache.addCollection(collection) }
  }

  override suspend fun getCollection(collectionId: String): Result<Collection> {
    var result = localSource.getCollection(collectionId)

    if (result.isFailure && sources.isRemoteSourceAvailable()) {
      result = remoteSource.getCollection(collectionId)
    }

    return result
  }

  override suspend fun updateCollection(
    collectionId: String,
    input: CollectionInput
  ): Result<Collection> {
    if (sources.isRemoteSourceEnabled()) {
      remoteSource.updateCollection(collectionId, input).guard { return it }
    }

    localSource.updateCollection(collectionId, input)
    val updatedCollection = localSource.getCollection(collectionId)
      .guard { return it }
    cache.updateCollection(updatedCollection)

    return Result.success(updatedCollection)
  }

  override suspend fun deleteCollection(collectionId: String): EmptyResult {
    if (sources.isRemoteSourceEnabled()) {
      remoteSource.deleteCollection(collectionId).guard { return it }
    }

    localSource.deleteCollection(collectionId)
    cache.removeCollection(collectionId)

    return successResult
  }

  override suspend fun clearUnusedCollections(): EmptyResult =
    localSource.clearUnusedCollections()
}
