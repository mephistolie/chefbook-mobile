package io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SignInRequest(
  @SerialName("email")
  val email: String? = null,
  @SerialName("nickname")
  val nickname: String? = null,
  @SerialName("password")
  val password: String,
)
