package io.chefbook.sdk.file.impl.di

import io.chefbook.sdk.file.api.internal.images.ImageCompressor
import io.chefbook.sdk.file.api.internal.io.IOProvider
import org.koin.core.scope.Scope

expect fun Scope.ioProvider(): IOProvider

expect fun Scope.imageCompressor(): ImageCompressor

