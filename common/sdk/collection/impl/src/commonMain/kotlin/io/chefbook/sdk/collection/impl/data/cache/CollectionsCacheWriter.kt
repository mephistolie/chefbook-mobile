package io.chefbook.sdk.collection.impl.data.cache

import io.chefbook.sdk.collection.api.external.domain.entities.Collection

interface CollectionsCacheWriter {
  suspend fun setCollections(categories: List<Collection>)
  suspend fun addCollection(collection: Collection)
  suspend fun updateCollection(collection: Collection)
  suspend fun removeCollection(collectionId: String)
}
