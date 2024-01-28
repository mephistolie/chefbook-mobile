package io.chefbook.sdk.file.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.sdk.file.api.internal.data.repositories.FileRepository
import io.chefbook.sdk.file.impl.data.repositories.FileRepositoryImpl
import io.chefbook.sdk.file.impl.data.sources.FileSource
import io.chefbook.sdk.file.impl.data.sources.local.LocalFileSource
import io.chefbook.sdk.file.impl.data.sources.local.LocalFileSourceImpl
import io.chefbook.sdk.file.impl.data.sources.remote.RemoteFileSourceImpl
import io.chefbook.sdk.file.impl.data.sources.remote.api.FileApiService
import io.chefbook.sdk.file.impl.data.sources.remote.api.FileApiServiceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val sdkFileModule = module {

  singleOf(::FileApiServiceImpl) bind FileApiService::class

  single<LocalFileSource>(named(DataSource.LOCAL)) { LocalFileSourceImpl(get(), get()) }
  single<FileSource>(named(DataSource.REMOTE)) { RemoteFileSourceImpl(get()) }

  single<FileRepository> {
    FileRepositoryImpl(
      get(named(DataSource.LOCAL)),
      get(named(DataSource.REMOTE))
    )
  }
}
