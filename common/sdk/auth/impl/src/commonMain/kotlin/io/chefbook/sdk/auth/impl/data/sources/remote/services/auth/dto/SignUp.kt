package io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SignUpRequest(
  @SerialName("userId")
  val userId: String? = null,
  @SerialName("email")
  val email: String,
  @SerialName("password")
  val password: String,
)

internal data class SignUpResponse(
  @SerialName("userId")
  val userId: String,
  @SerialName("activated")
  val activated: Boolean,
  @SerialName("message")
  val message: String? = null,
)
