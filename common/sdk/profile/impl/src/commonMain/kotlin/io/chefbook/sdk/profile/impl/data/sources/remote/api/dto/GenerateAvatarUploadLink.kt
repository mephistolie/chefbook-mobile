package io.chefbook.sdk.profile.impl.data.sources.remote.api.dto

import io.chefbook.sdk.core.api.internal.data.models.PictureUploading
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerateAvatarUploadLinkRequest(
  @SerialName("pictureLink")
  val pictureLink: String,
  @SerialName("uploadLink")
  val uploadLink: String,
  @SerialName("formData")
  val formData: Map<String, String>,
  @SerialName("maxSize")
  val maxSize: Long,
) {

  fun toEntity() = PictureUploading(
    picturePath = pictureLink,
    uploadPath = uploadLink,
    meta = formData,
    maxSize = maxSize,
  )
}
