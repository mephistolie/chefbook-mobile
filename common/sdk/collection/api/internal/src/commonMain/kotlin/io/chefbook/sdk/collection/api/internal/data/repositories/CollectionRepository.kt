package io.chefbook.sdk.collection.api.internal.data.repositories

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

  fun observeCollections(): Flow<List<Collection>?>

  suspend fun getCollections(): List<Collection>

  suspend fun cacheCollections(categories: List<Collection>)

  suspend fun createCollection(input: CollectionInput): Result<String>

  suspend fun getCollection(collectionId: String): Result<Collection>

  suspend fun updateCollection(collectionId: String, input: CollectionInput): Result<Collection>

  suspend fun deleteCollection(collectionId: String): EmptyResult

  suspend fun clearUnusedCollections(): EmptyResult
}
