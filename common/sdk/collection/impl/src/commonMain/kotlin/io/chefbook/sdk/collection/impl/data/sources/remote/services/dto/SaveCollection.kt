package io.chefbook.sdk.collection.impl.data.sources.remote.services.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class SaveCollectionRequestBody(
  @SerialName("contributorKey")
  val contributorKey: String?,
)
