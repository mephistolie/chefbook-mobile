package io.chefbook.sdk.recipe.crud.impl.data.models

data class PictureUploading(
  val picturePath: String,
  val uploadPath: String = picturePath,
  val meta: Map<String, String> = emptyMap(),
  val maxSize: Long? = null,
)
