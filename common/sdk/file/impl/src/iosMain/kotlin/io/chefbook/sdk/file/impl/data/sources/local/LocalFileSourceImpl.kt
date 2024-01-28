package io.chefbook.sdk.file.impl.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult

internal class LocalFileSourceImpl : LocalFileSource {

  override suspend fun getFile(path: String): Result<ByteArray> = TODO()

  override suspend fun uploadFile(
    path: String,
    file: ByteArray,
    meta: Map<String, String>
  ): EmptyResult = TODO()

  override suspend fun compressImage(
    path: String,
    width: Int,
    height: Int,
    quality: Int,
    maxFileSize: Long?
  ): Result<String> {
    TODO("Not yet implemented")
  }

  override suspend fun deleteFile(path: String): EmptyResult {
    TODO("Not yet implemented")
  }

  override suspend fun deleteCachedFile(relativePath: String): EmptyResult {
    TODO("Not yet implemented")
  }
}
