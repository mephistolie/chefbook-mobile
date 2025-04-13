package io.chefbook.sdk.collection.impl.data.sources.remote

import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput
import io.chefbook.sdk.collection.impl.data.sources.remote.services.CollectionApiService
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.CreateCollectionResponseBody
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.GetCollectionResponseBody
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.GetCollectionsResponseBody
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.deserialize
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.toCreateCollectionRequest
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.toUpdateCollectionRequest

internal class RemoteCollectionSourceImpl(
  private val api: CollectionApiService,
) : RemoteCollectionSource {

  override suspend fun getCollections(): Result<List<Collection>> =
    api.getCollections().map(GetCollectionsResponseBody::deserialize)

  override suspend fun createCollection(input: CollectionInput) =
    api.createCollection(input.toCreateCollectionRequest())
      .map(CreateCollectionResponseBody::collectionId)

  override suspend fun getCollection(collectionId: String) =
    api.getCollection(collectionId).map(GetCollectionResponseBody::deserialize)

  override suspend fun updateCollection(collectionId: String, input: CollectionInput) =
    api.updateCollection(collectionId, input.toUpdateCollectionRequest())
      .asEmpty()

  override suspend fun deleteCollection(collectionId: String) =
    api.deleteCollection(collectionId).asEmpty()
}
