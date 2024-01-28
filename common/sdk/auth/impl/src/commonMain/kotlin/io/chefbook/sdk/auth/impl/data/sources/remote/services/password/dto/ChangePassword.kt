package io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ChangePasswordRequest(
  @SerialName("oldPassword")
  val oldPassword: String,
  @SerialName("newPassword")
  val newPassword: String,
)
