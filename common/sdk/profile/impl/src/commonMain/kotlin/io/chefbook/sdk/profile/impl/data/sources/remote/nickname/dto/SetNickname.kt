package io.chefbook.sdk.profile.impl.data.sources.remote.nickname.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SetNicknameRequest(
  @SerialName("nickname")
  val nickname: String,
)
