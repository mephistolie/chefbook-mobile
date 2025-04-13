package io.chefbook.sdk.tag.impl.di

import androidx.datastore.core.DataStore
import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.libs.di.scopes.ProfileScope
import io.chefbook.sdk.database.api.internal.ChefBookDataStoreFactory
import io.chefbook.sdk.tag.impl.data.sources.remote.RemoteTagSourceImpl
import io.chefbook.sdk.tag.api.external.domain.usecases.GetTagsUseCase
import io.chefbook.sdk.tag.api.external.domain.usecases.ObserveTagsUseCase
import io.chefbook.sdk.tag.api.internal.data.repositories.TagRepository
import io.chefbook.sdk.tag.api.internal.data.sources.common.dto.TagsSerializable
import io.chefbook.sdk.tag.impl.data.repositories.TagRepositoryImpl
import io.chefbook.sdk.tag.impl.data.sources.local.LocalTagSource
import io.chefbook.sdk.tag.impl.data.sources.local.LocalTagSourceImpl
import io.chefbook.sdk.tag.impl.data.sources.local.datastore.TagsSerializer
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

  singleOf(::tagsDataStore)

  scope<ProfileScope> {
    scopedOf(::TagApiServiceImpl) bind TagApiService::class

    scoped<LocalTagSource>(named(DataSource.LOCAL)) { LocalTagSourceImpl(get()) }
    scoped<RemoteTagSource>(named(DataSource.REMOTE)) { RemoteTagSourceImpl(get()) }

    scoped<TagRepository> {
      TagRepositoryImpl(
        localSource = get(named(DataSource.LOCAL)),
        remoteSource = get(named(DataSource.REMOTE)),
        dispatchers = get(),
        scopes = get(),
      )
    }

    factoryOf(::ObserveTagsUseCaseImpl) bind ObserveTagsUseCase::class
    factoryOf(::GetTagsUseCaseImpl) bind GetTagsUseCase::class
  }
}

private fun tagsDataStore(
  factory: ChefBookDataStoreFactory,
): DataStore<TagsSerializable> =
  factory.create(
    fileName = "tags.json",
    serializer = TagsSerializer,
  )
