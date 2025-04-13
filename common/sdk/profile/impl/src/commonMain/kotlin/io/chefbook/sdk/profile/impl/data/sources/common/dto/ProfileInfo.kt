package io.chefbook.sdk.profile.impl.data.sources.common.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileInfoSerializable(
  @SerialName("name")
  val name: String? = null,
  @SerialName("avatar")
  val avatar: String? = null,
)
