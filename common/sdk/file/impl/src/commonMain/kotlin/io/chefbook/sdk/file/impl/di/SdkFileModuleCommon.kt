package io.chefbook.sdk.file.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.sdk.file.api.internal.data.repositories.FileRepository
import io.chefbook.sdk.file.api.internal.images.ImageCompressor
import io.chefbook.sdk.file.api.internal.io.IOProvider
import io.chefbook.sdk.file.impl.data.repositories.FileRepositoryImpl
import io.chefbook.sdk.file.impl.data.sources.FileSource
import io.chefbook.sdk.file.impl.data.sources.local.LocalFileSource
import io.chefbook.sdk.file.impl.data.sources.local.LocalFileSourceImpl
import io.chefbook.sdk.file.impl.data.sources.remote.RemoteFileSourceImpl
import io.chefbook.sdk.file.impl.data.sources.remote.api.FileApiService
import io.chefbook.sdk.file.impl.data.sources.remote.api.FileApiServiceImpl
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

fun sdkFileModule() = module {

  single<IOProvider> { ioProvider() }
  single<ImageCompressor> { imageCompressor() }

  single<FileApiService> {
    FileApiServiceImpl(
      client = get(qualifier = named(io.chefbook.libs.di.qualifiers.HttpClient.BASE)),
    )
  }

  single<LocalFileSource>(named(DataSource.LOCAL)) { LocalFileSourceImpl(get(), get()) }
  single<FileSource>(named(DataSource.REMOTE)) { RemoteFileSourceImpl(get()) }

  single<FileRepository> {
    FileRepositoryImpl(
      get(named(DataSource.LOCAL)),
      get(named(DataSource.REMOTE))
    )
  }
}
