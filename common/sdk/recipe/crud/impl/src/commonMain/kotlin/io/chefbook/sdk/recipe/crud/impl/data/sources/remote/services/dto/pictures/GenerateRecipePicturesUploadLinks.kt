package io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.pictures

import io.chefbook.sdk.recipe.crud.impl.data.models.PictureUploading
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GenerateRecipePicturesUploadLinksRequest(
  @SerialName("picturesCount")
  val picturesCount: Int,
)

@Serializable
internal data class PictureUploadResponse(
  @SerialName("pictureLink")
  val pictureLink: String,
  @SerialName("uploadLink")
  val uploadLink: String,
  @SerialName("formData")
  val formData: Map<String, String>,
  @SerialName("maxSize")
  val maxSize: Long,
)

internal fun PictureUploadResponse.toModel() =
  PictureUploading(
    picturePath = pictureLink,
    uploadPath = uploadLink,
    meta = formData,
    maxSize = maxSize,
  )
