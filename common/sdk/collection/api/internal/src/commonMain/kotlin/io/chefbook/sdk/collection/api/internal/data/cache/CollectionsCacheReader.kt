package io.chefbook.sdk.collection.api.internal.data.cache

import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import kotlinx.coroutines.flow.StateFlow

interface CollectionsCacheReader {

  fun observeCollections(): StateFlow<List<Collection>?>

  suspend fun getCollections(): List<Collection>

  suspend fun getCollection(collectionId: String): Collection?
}
