package io.chefbook.sdk.profile.impl.data.sources.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfirmAvatarUploadingRequest(
  @SerialName("avatarLink")
  val avatarLink: String,
)