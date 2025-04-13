package io.chefbook.sdk.collection.impl.data.sources.remote.services.dto

import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class UpdateCollectionRequestBody(
  @SerialName("name")
  val name: String,
  @SerialName("visibility")
  val visibility: String,
)

internal fun CollectionInput.toUpdateCollectionRequest() =
  UpdateCollectionRequestBody(
    name = name,
    visibility = visibility.serialize(),
  )
