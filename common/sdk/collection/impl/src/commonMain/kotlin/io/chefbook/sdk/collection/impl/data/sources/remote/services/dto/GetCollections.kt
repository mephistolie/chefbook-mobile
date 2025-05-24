package io.chefbook.sdk.collection.impl.data.sources.remote.services.dto

import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.collection.api.internal.data.sources.remote.services.dto.CollectionSerializable
import io.chefbook.sdk.network.api.internal.service.dto.responses.ProfileMinInfoSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class GetCollectionsResponseBody(
  @SerialName("collections")
  val collections: List<CollectionSerializable>,
  @SerialName("profilesInfo")
  val profilesInfo: Map<String, ProfileMinInfoSerializable>,
)

internal fun GetCollectionsResponseBody.deserialize(): List<Collection> =
  collections.map { collection -> collection.deserialize(profilesInfo) }
