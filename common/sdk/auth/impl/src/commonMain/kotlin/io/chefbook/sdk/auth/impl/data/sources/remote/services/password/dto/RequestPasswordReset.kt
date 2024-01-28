package io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RequestPasswordResetRequest(
  @SerialName("email")
  val email: String? = null,
  @SerialName("nickname")
  val nickname: String? = null,
)
