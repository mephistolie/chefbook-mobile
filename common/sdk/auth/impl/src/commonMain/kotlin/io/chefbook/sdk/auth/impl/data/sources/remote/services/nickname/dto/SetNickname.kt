package io.chefbook.sdk.auth.impl.data.sources.remote.services.nickname.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SetNicknameRequest(
  @SerialName("nickname")
  val nickname: String,
)
