package io.chefbook.sdk.file.impl.di

import io.chefbook.sdk.file.api.internal.images.ImageCompressor
import io.chefbook.sdk.file.api.internal.io.IOProvider
import io.chefbook.sdk.file.impl.io.IOProviderImpl
import org.koin.core.scope.Scope

actual fun Scope.ioProvider(): IOProvider =
  IOProviderImpl()

actual fun Scope.imageCompressor(): ImageCompressor =
  object : ImageCompressor {

    override suspend fun compressImage(
      path: String,
      width: Int,
      height: Int,
      quality: Int,
      maxFileSize: Long?
    ): Result<String> {
      // TODO: compress image
      return Result.success(path)
    }
  }
