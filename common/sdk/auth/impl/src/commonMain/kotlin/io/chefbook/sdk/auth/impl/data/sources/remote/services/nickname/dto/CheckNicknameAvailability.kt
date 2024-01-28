package io.chefbook.sdk.auth.impl.data.sources.remote.services.nickname.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CheckNicknameRequest(
  @SerialName("nickname")
  val nickname: String,
)

@Serializable
internal data class CheckNicknameResponse(
  @SerialName("available")
  val available: Boolean,
)
