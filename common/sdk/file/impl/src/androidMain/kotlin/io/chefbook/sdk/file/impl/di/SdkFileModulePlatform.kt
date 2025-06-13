package io.chefbook.sdk.file.impl.di

import io.chefbook.sdk.file.api.internal.images.ImageCompressor
import io.chefbook.sdk.file.api.internal.io.IOProvider
import io.chefbook.sdk.file.impl.images.ImageCompressorImpl
import io.chefbook.sdk.file.impl.io.IOProviderImpl
import org.koin.core.scope.Scope

actual fun Scope.ioProvider(): IOProvider =
  IOProviderImpl(
    context = get(),
  )

actual fun Scope.imageCompressor(): ImageCompressor =
  ImageCompressorImpl(
    context = get(),
    dispatchers = get(),
  )
