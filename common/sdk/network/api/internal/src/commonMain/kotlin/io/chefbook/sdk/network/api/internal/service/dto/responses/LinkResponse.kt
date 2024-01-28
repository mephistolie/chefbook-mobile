package io.chefbook.sdk.network.api.internal.service.dto.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinkResponse(
  @SerialName("link")
  val link: String,
)
