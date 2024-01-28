package io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SignOutRequest(
  @SerialName("refreshToken")
  val refreshToken: String
)
