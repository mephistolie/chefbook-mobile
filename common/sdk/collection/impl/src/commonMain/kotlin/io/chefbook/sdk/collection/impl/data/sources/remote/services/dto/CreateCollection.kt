package io.chefbook.sdk.collection.impl.data.sources.remote.services.dto

import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class CreateCollectionRequestBody(
  @SerialName("collectionId")
  val collectionId: String,
  @SerialName("name")
  val name: String,
  @SerialName("visibility")
  val visibility: String,
)

@Serializable
internal class CreateCollectionResponseBody(
  @SerialName("collectionId")
  val collectionId: String,
)

internal fun CollectionInput.toCreateCollectionRequest() =
  CreateCollectionRequestBody(
    collectionId = id,
    name = name,
    visibility = visibility.serialize(),
  )
