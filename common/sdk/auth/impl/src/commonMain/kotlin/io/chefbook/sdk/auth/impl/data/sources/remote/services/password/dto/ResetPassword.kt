package io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ResetPasswordRequest(
  @SerialName("userId")
  val userId: String,
  @SerialName("resetCode")
  val resetCode: String,
  @SerialName("newPassword")
  val newPassword: String,
)
