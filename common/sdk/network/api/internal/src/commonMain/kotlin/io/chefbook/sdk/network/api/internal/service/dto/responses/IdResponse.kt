package io.chefbook.sdk.network.api.internal.service.dto.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IdResponse(
  @SerialName("id")
  val id: String,
  @SerialName("message")
  val message: String = "",
)
