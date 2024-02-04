package io.chefbook.sdk.core.api.internal.data.models

data class PictureUploading(
  val picturePath: String,
  val uploadPath: String = picturePath,
  val meta: Map<String, String> = emptyMap(),
  val maxSize: Long? = null,
)
