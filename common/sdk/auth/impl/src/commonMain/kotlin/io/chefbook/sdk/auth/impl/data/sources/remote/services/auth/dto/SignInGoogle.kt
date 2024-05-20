package io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SignInGoogleRequest(
  @SerialName("idToken")
  val idToken: String? = null,
  @SerialName("code")
  val code: String? = null,
  @SerialName("state")
  val state: String? = null,
)
