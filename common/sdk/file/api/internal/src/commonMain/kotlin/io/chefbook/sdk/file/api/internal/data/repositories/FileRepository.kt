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

  suspend fun deleteFile(path: String): EmptyResult

  suspend fun deleteCachedFile(relativePath: String): EmptyResult
}
