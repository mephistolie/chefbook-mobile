package io.chefbook.sdk.file.api.internal.data.repositories

import io.chefbook.libs.utils.result.EmptyResult

interface FileRepository {

  suspend fun getFile(path: String): Result<ByteArray>

  suspend fun uploadFile(
    path: String,
    file: ByteArray,
    meta: Map<String, String> = emptyMap(),
  ): EmptyResult

  suspend fun isRemoteSource(path: String): Boolean

  suspend fun compressImage(
    path: String,
    width: Int = 1440,
    height: Int = 1440,
    quality: Int = 100,
    maxFileSize: Long? = null,
  ): Result<String>

  suspend fun deleteFile(path: String): EmptyResult

  suspend fun deleteCachedFile(relativePath: String): EmptyResult
}
