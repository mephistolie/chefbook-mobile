package io.chefbook.sdk.profile.impl.data.sources.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RequestProfileDeletionRequest(
  @SerialName("password")
  val password: String? = null,
  @SerialName("withSharedData")
  val withSharedData: Boolean = false,
)

@Serializable
internal data class RequestProfileDeletionResponse(
  @SerialName("deletionTimestamp")
  val deletionTimestamp: String,
)
