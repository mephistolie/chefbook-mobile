package io.chefbook.sdk.profile.impl.data.sources.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ChangePasswordRequest(
  @SerialName("oldPassword")
  val oldPassword: String,
  @SerialName("newPassword")
  val newPassword: String,
) 
