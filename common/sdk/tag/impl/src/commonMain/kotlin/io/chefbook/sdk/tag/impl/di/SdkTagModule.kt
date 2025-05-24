package io.chefbook.sdk.tag.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.libs.di.scopes.ProfileComponent
import io.chefbook.sdk.tag.impl.data.sources.remote.RemoteTagSourceImpl
import io.chefbook.sdk.tag.api.external.domain.usecases.GetTagsUseCase
import io.chefbook.sdk.tag.api.external.domain.usecases.ObserveTagsUseCase
import io.chefbook.sdk.tag.api.internal.data.repositories.TagRepository
import io.chefbook.sdk.tag.impl.data.repositories.TagRepositoryImpl
import io.chefbook.sdk.tag.impl.data.sources.local.LocalTagSource
import io.chefbook.sdk.tag.impl.data.sources.local.LocalTagSourceImpl
import io.chefbook.sdk.tag.impl.data.sources.local.datastore.TagsDataStore
import io.chefbook.sdk.tag.impl.data.sources.local.datastore.TagsDataStoreImpl
import io.chefbook.sdk.tag.impl.data.sources.remote.RemoteTagSource
import io.chefbook.sdk.tag.impl.data.sources.remote.services.TagApiService
import io.chefbook.sdk.tag.impl.data.sources.remote.services.TagApiServiceImpl
import io.chefbook.sdk.tag.impl.domain.GetTagsUseCaseImpl
import io.chefbook.sdk.tag.impl.domain.ObserveTagsUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

fun sdkTagModule() = module {

  singleOf(::TagsDataStoreImpl) bind TagsDataStore::class

  scope<ProfileComponent> {
    scopedOf(::TagApiServiceImpl) bind TagApiService::class

    scoped<LocalTagSource>(named(DataSource.LOCAL)) { LocalTagSourceImpl(get()) }
    scoped<RemoteTagSource>(named(DataSource.REMOTE)) { RemoteTagSourceImpl(get()) }

    scoped<TagRepository> {
      TagRepositoryImpl(
        localSource = get(named(DataSource.LOCAL)),
        remoteSource = get(named(DataSource.REMOTE)),
        dispatchers = get(),
        profileScope = get<ProfileComponent>().coroutineScope,
      )
    }

    factoryOf(::ObserveTagsUseCaseImpl) bind ObserveTagsUseCase::class
    factoryOf(::GetTagsUseCaseImpl) bind GetTagsUseCase::class
  }
}
