package io.chefbook.sdk.collection.impl.data.sources.remote.services

import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.CollectionSerializable
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.CreateCollectionRequestBody
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.CreateCollectionResponseBody
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.GetCollectionResponseBody
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.GetCollectionsResponseBody
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.UpdateCollectionRequestBody
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse

internal interface CollectionApiService {

  suspend fun getCollections(): Result<GetCollectionsResponseBody>

  suspend fun createCollection(
    body: CreateCollectionRequestBody,
  ): Result<CreateCollectionResponseBody>

  suspend fun getCollection(
    collectionId: String,
  ): Result<GetCollectionResponseBody>

  suspend fun updateCollection(
    collectionId: String,
    body: UpdateCollectionRequestBody
  ): Result<MessageResponse>

  suspend fun deleteCollection(collectionId: String): Result<MessageResponse>
}
