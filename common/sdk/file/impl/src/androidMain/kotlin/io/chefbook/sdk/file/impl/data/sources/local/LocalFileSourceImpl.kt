package io.chefbook.sdk.file.impl.data.sources.local

import android.content.Context
import android.content.res.Resources.NotFoundException
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import kotlinx.coroutines.withContext
import java.io.File

internal class LocalFileSourceImpl(
  private val dispatchers: AppDispatchers,
  private val context: Context,
) : LocalFileSource {

  override suspend fun getFile(path: String): Result<ByteArray> =
    withContext(dispatchers.io) {
      val file = File(path)
      if (!file.exists()) return@withContext Result.failure(NotFoundException())
      return@withContext try {
        Result.success(file.readBytes())
      } catch (e: OutOfMemoryError) {
        Result.failure(e)
      }
    }

  override suspend fun uploadFile(
    path: String,
    file: ByteArray,
    meta: Map<String, String>
  ): EmptyResult = withContext(dispatchers.io) {
    return@withContext try {
      val fileDir = File(context.filesDir, path.substringBeforeLast("/"))
      if (!fileDir.exists()) fileDir.mkdirs()
      val recipePicture = File(fileDir, path.substringAfterLast("/"))
      recipePicture.writeBytes(file)
      successResult
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  override suspend fun compressImage(
    path: String,
    width: Int,
    height: Int,
    quality: Int,
    maxFileSize: Long?,
  ): Result<String> = withContext(dispatchers.io) {
    runCatching {
      val file = File(path)

      val compressedFile = Compressor.compress(context, file) {
        resolution(width, height)
        quality(quality)
        maxFileSize?.let { size(it) }
        destination(File("${file.parent}/${file.nameWithoutExtension}_cropped"))
      }

      compressedFile.path.also { file.delete() }
    }
  }

  override suspend fun deleteFile(path: String) = runCatching {
    val file = File(path)
    if (file.exists()) file.deleteRecursively()
  }

  override suspend fun deleteCachedFile(relativePath: String) = runCatching {
    val file = File(context.cacheDir, relativePath)
    if (file.exists()) file.deleteRecursively()
  }
}
