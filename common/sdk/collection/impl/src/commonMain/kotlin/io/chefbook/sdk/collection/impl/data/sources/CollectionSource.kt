package io.chefbook.sdk.collection.impl.data.sources

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput

internal interface CollectionSource {

  suspend fun getCollections(): Result<List<Collection>>

  suspend fun getCollection(collectionId: String): Result<Collection>

  suspend fun updateCollection(collectionId: String, input: CollectionInput): EmptyResult

  suspend fun deleteCollection(collectionId: String): EmptyResult
}
