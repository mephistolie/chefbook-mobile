package io.chefbook.sdk.collection.impl.data.sources.remote.services

import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.CreateCollectionRequestBody
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.CreateCollectionResponseBody
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.GetCollectionResponseBody
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.GetCollectionsResponseBody
import io.chefbook.sdk.collection.impl.data.sources.remote.services.dto.UpdateCollectionRequestBody
import io.chefbook.sdk.network.api.internal.service.ApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

internal class CollectionApiServiceImpl(
  client: HttpClient,
) : ApiService(client), CollectionApiService {

  override suspend fun getCollections(): Result<GetCollectionsResponseBody> =
    safeGet(COLLECTIONS_ROUTE)

  override suspend fun createCollection(
    body: CreateCollectionRequestBody,
  ): Result<CreateCollectionResponseBody> =
    safePost(COLLECTIONS_ROUTE) { setBody(body) }

  override suspend fun getCollection(
    collectionId: String,
  ): Result<GetCollectionResponseBody> =
    safeGet("$COLLECTIONS_ROUTE/$collectionId")

  override suspend fun updateCollection(
    collectionId: String,
    body: UpdateCollectionRequestBody
  ): Result<MessageResponse> =
    safePut("$COLLECTIONS_ROUTE/$collectionId") { setBody(body) }

  override suspend fun deleteCollection(
    collectionId: String,
  ): Result<MessageResponse> =
    safeDelete("$COLLECTIONS_ROUTE/$collectionId")

  companion object {
    private const val COLLECTIONS_ROUTE = "/v1/collections"
  }
}
