package io.chefbook.sdk.profile.impl.data.sources.remote.nickname.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CheckNicknameAvailabilityRequest(
  @SerialName("nickname")
  val nickname: String,
)

@Serializable
internal data class CheckNicknameAvailabilityResponse(
  @SerialName("available")
  val available: Boolean,
)
