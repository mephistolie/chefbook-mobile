package io.chefbook.sdk.network.api.internal.service.dto.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
  @SerialName("error")
  val error: String = "",
  @SerialName("message")
  val message: String? = null,
)
