package io.chefbook.sdk.file.impl.images

import android.content.Context
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.sdk.file.api.internal.images.ImageCompressor
import kotlinx.coroutines.withContext
import java.io.File

internal class ImageCompressorImpl(
  private val context: Context,
  private val dispatchers: AppDispatchers,
) : ImageCompressor {

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
}
