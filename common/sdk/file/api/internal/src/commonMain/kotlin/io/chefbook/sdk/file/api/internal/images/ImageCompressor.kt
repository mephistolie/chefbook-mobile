package io.chefbook.sdk.file.api.internal.images

interface ImageCompressor {

  suspend fun compressImage(
    path: String,
    width: Int = 1284,
    height: Int = 1284,
    quality: Int = 100,
    maxFileSize: Long? = null,
  ): Result<String>
}
