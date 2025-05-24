package io.chefbook.sdk.collection.impl.data.cache

import io.chefbook.libs.logger.Logger
import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class CollectionsCacheImpl : CollectionsCache {

  private val cache = MutableStateFlow<List<Collection>?>(null)

  override fun observeCollections(): StateFlow<List<Collection>?> = cache.asStateFlow()

  override suspend fun getCollections(): List<Collection> = cache.value.orEmpty()

  override suspend fun getCollection(collectionId: String) =
    getCollections().first { it.id == collectionId }

  override suspend fun setCollections(categories: List<Collection>) {
    cache.emit(categories)
    Logger.d("Collections set: ${categories.map { it.id }}")
  }

  override suspend fun addCollection(collection: Collection) {
    cache.update { categories ->
      categories?.filter { it.id != collection.id }.orEmpty().plus(collection)
    }
    Logger.d("Collection added: ${collection.id}")
  }

  override suspend fun updateCollection(collection: Collection) {
    cache.update { categories ->
      categories?.map { if (it.id != collection.id) it else collection }
    }
    Logger.d("Collection updated: ${collection.id}")
  }

  override suspend fun removeCollection(collectionId: String) {
    cache.update { categories -> categories?.filter { it.id != collectionId } }
    Logger.d("Collection removed: $collectionId")
  }
}
