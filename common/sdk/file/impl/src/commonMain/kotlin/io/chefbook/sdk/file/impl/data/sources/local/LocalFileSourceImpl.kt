package io.chefbook.sdk.file.impl.data.sources.local

import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.sdk.file.api.internal.io.IOProvider
import io.chefbook.libs.exceptions.NotFoundException
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import kotlinx.coroutines.withContext
import okio.BufferedSource
import okio.Path.Companion.toPath

internal class LocalFileSourceImpl(
  io: IOProvider,
  private val dispatchers: AppDispatchers,
) : LocalFileSource {

  private val files = io.fileSystem
  private val filesDir = io.filesDir
  private val cacheDir = io.cacheDir

  override suspend fun getFile(path: String): Result<ByteArray> =
    withContext(dispatchers.io) {
      val filePath = path.toPath()
      files.exists(filePath)
      if (!files.exists(filePath)) return@withContext Result.failure(NotFoundException())
      return@withContext try {
        val file = files.read(filePath, BufferedSource::readByteArray)
        Result.success(file)
      } catch (e: Exception) {
        Result.failure(e)
      }
    }

  override suspend fun uploadFile(
    path: String,
    file: ByteArray,
    meta: Map<String, String>
  ): EmptyResult = withContext(dispatchers.io) {
    return@withContext try {
      val fileDir = filesDir.resolve(path.substringBeforeLast("/"))
      if (!files.exists(fileDir)) files.createDirectories(fileDir)
      val filePath = fileDir.resolve(path.substringAfterLast("/"))
      files.write(filePath) { write(file) }
      successResult
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  override suspend fun deleteFile(path: String) = runCatching {
    val filePath = path.toPath()
    if (files.exists(filePath)) files.deleteRecursively(filePath)
  }

  override suspend fun deleteCachedFile(relativePath: String) = runCatching {
    val filePath = cacheDir.resolve(relativePath)
    if (files.exists(filePath)) files.deleteRecursively(filePath)
  }
}
