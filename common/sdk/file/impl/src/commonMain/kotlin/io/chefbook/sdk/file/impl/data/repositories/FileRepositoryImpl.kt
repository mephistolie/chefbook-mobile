package io.chefbook.sdk.file.impl.data.repositories

import io.chefbook.libs.logger.Logger
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.url.isValidUrl
import io.chefbook.sdk.file.api.internal.data.repositories.FileRepository
import io.chefbook.sdk.file.impl.data.sources.FileSource
import io.chefbook.sdk.file.impl.data.sources.local.LocalFileSource

internal class FileRepositoryImpl(
  private val local: LocalFileSource,
  private val remote: FileSource,
) : FileRepository {

  override suspend fun getFile(path: String): Result<ByteArray> {
    val result = if (isRemoteSource(path)) {
      remote.getFile(path)
    } else {
      local.getFile(path)
    }
    if (result.isSuccess) {
      Logger.i { "Got file $path" }
    } else {
      Logger.e(result.exceptionOrNull()) { "Unable to get file $path"  }
    }

    return result
  }

  override suspend fun uploadFile(
    path: String,
    file: ByteArray,
    meta: Map<String, String>,
  ): EmptyResult {
    return if (isRemoteSource(path)) {
      remote.uploadFile(path, file, meta)
    } else {
      local.uploadFile(path, file, meta)
    }
  }

  override suspend fun isRemoteSource(path: String): Boolean = isValidUrl(path)

  override suspend fun deleteFile(path: String) = local.deleteFile(path)

  override suspend fun deleteCachedFile(relativePath: String) =
    local.deleteCachedFile(relativePath)
}
