package io.chefbook.sdk.collection.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.libs.di.scopes.ProfileScope
import io.chefbook.sdk.collection.api.external.domain.usecases.CreateCollectionUseCase
import io.chefbook.sdk.collection.api.external.domain.usecases.DeleteCollectionUseCase
import io.chefbook.sdk.collection.api.external.domain.usecases.GetCollectionsUseCase
import io.chefbook.sdk.collection.api.external.domain.usecases.GetCollectionUseCase
import io.chefbook.sdk.collection.api.external.domain.usecases.ObserveCollectionsUseCase
import io.chefbook.sdk.collection.api.external.domain.usecases.UpdateCollectionUseCase
import io.chefbook.sdk.collection.impl.data.cache.CollectionsCache
import io.chefbook.sdk.collection.api.internal.data.cache.CollectionsCacheReader
import io.chefbook.sdk.collection.impl.data.cache.CollectionsCacheWriter
import io.chefbook.sdk.collection.api.internal.data.repositories.CollectionRepository
import io.chefbook.sdk.collection.impl.data.cache.CollectionsCacheImpl
import io.chefbook.sdk.collection.impl.data.repositories.CollectionRepositoryImpl
import io.chefbook.sdk.collection.impl.data.sources.local.LocalCollectionSource
import io.chefbook.sdk.collection.impl.data.sources.local.LocalCollectionSourceImpl
import io.chefbook.sdk.collection.impl.data.sources.remote.RemoteCollectionSource
import io.chefbook.sdk.collection.impl.data.sources.remote.RemoteCollectionSourceImpl
import io.chefbook.sdk.collection.impl.data.sources.remote.services.CollectionApiService
import io.chefbook.sdk.collection.impl.data.sources.remote.services.CollectionApiServiceImpl
import io.chefbook.sdk.collection.impl.domain.CreateCollectionUseCaseImpl
import io.chefbook.sdk.collection.impl.domain.DeleteCollectionUseCaseImpl
import io.chefbook.sdk.collection.impl.domain.GetCollectionsUseCaseImpl
import io.chefbook.sdk.collection.impl.domain.GetCollectionUseCaseImpl
import io.chefbook.sdk.collection.impl.domain.ObserveCollectionsUseCaseImpl
import io.chefbook.sdk.collection.impl.domain.UpdateCollectionUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

fun sdkCollectionModule() = module {

  scope<ProfileScope> {

    scopedOf(::CollectionsCacheImpl) binds arrayOf(
      CollectionsCache::class,
      CollectionsCacheReader::class,
      CollectionsCacheWriter::class
    )

    scopedOf(::CollectionApiServiceImpl) bind CollectionApiService::class

    scoped<LocalCollectionSource>(named(DataSource.LOCAL)) { params ->
      LocalCollectionSourceImpl(
        profileId = params[ProfileScope.PARAM_PROFILE_ID],
        database = get(),
      )
    }

    scoped<RemoteCollectionSource>(named(DataSource.LOCAL)) {
      RemoteCollectionSourceImpl(
        api = get(),
      )
    }

    scoped<CollectionRepository> {
      CollectionRepositoryImpl(
        localSource = get(named(DataSource.LOCAL)),
        remoteSource = get(named(DataSource.REMOTE)),
        profileRepository = get(),
        sources = get(),
        cache = get(),
        scopes = get(),
        dispatchers = get(),
      )
    }

    factoryOf(::ObserveCollectionsUseCaseImpl) bind ObserveCollectionsUseCase::class
    factoryOf(::GetCollectionsUseCaseImpl) bind GetCollectionsUseCase::class
    factoryOf(::GetCollectionUseCaseImpl) bind GetCollectionUseCase::class
    factoryOf(::CreateCollectionUseCaseImpl) bind CreateCollectionUseCase::class
    factoryOf(::UpdateCollectionUseCaseImpl) bind UpdateCollectionUseCase::class
    factoryOf(::DeleteCollectionUseCaseImpl) bind DeleteCollectionUseCase::class
  }
}
