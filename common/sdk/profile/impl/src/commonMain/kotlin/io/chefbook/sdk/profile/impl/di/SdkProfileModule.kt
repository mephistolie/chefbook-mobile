package io.chefbook.sdk.profile.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.libs.di.scopes.ProfileComponent
import io.chefbook.sdk.profile.api.external.domain.usecases.CancelProfileDeletionUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.CheckNicknameAvailabilityUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.DeleteAvatarUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.RequestProfileDeletionUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.SetAvatarUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.SetDescriptionUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.SetNameUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.SetNicknameUseCase
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfilesRepository
import io.chefbook.sdk.profile.impl.data.repositories.ProfileRepositoryImpl
import io.chefbook.sdk.profile.impl.data.repositories.ProfilesRepositoryImpl
import io.chefbook.sdk.profile.impl.data.repositories.PulledProfilesRepository
import io.chefbook.sdk.profile.impl.data.repositories.PulledProfilesRepositoryImpl
import io.chefbook.sdk.profile.impl.data.sources.common.ProfileSource
import io.chefbook.sdk.profile.impl.data.sources.local.LocalProfileSourceImpl
import io.chefbook.sdk.profile.impl.data.sources.local.LocalProfilesSource
import io.chefbook.sdk.profile.impl.data.sources.local.LocalProfilesSourceImpl
import io.chefbook.sdk.profile.impl.data.sources.local.datastore.ProfilesDataStore
import io.chefbook.sdk.profile.impl.data.sources.local.datastore.ProfilesDataStoreImpl
import io.chefbook.sdk.profile.impl.data.sources.remote.RemoteProfilesSourceImpl
import io.chefbook.sdk.profile.impl.data.sources.remote.RemoteProfileSource
import io.chefbook.sdk.profile.impl.data.sources.remote.RemoteProfileSourceImpl
import io.chefbook.sdk.profile.impl.data.sources.remote.RemoteProfilesSource
import io.chefbook.sdk.profile.impl.data.sources.remote.api.ProfileApiService
import io.chefbook.sdk.profile.impl.data.sources.remote.api.ProfileApiServiceImpl
import io.chefbook.sdk.profile.impl.data.sources.remote.api.ProfilesApiService
import io.chefbook.sdk.profile.impl.data.sources.remote.api.ProfilesApiServiceImpl
import io.chefbook.sdk.profile.impl.data.sources.remote.api.nickname.NicknameApiService
import io.chefbook.sdk.profile.impl.data.sources.remote.api.nickname.NicknameApiServiceImpl
import io.chefbook.sdk.profile.impl.domain.usecases.CancelProfileDeletionUseCaseImpl
import io.chefbook.sdk.profile.impl.domain.usecases.CheckNicknameAvailabilityUseCaseImpl
import io.chefbook.sdk.profile.impl.domain.usecases.DeleteAvatarUseCaseImpl
import io.chefbook.sdk.profile.impl.domain.usecases.ObserveProfileUseCaseImpl
import io.chefbook.sdk.profile.impl.domain.usecases.RequestProfileDeletionUseCaseImpl
import io.chefbook.sdk.profile.impl.domain.usecases.SetAvatarUseCaseImpl
import io.chefbook.sdk.profile.impl.domain.usecases.SetDescriptionUseCaseImpl
import io.chefbook.sdk.profile.impl.domain.usecases.SetNameUseCaseImpl
import io.chefbook.sdk.profile.impl.domain.usecases.SetNicknameUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

fun sdkProfileModule() = module {

  singleOf(::ProfilesDataStoreImpl) bind ProfilesDataStore::class

  singleOf(::ProfilesApiServiceImpl) bind ProfilesApiService::class

  singleOf(::LocalProfilesSourceImpl) { named(DataSource.LOCAL) } bind LocalProfilesSource::class
  singleOf(::RemoteProfilesSourceImpl) { named(DataSource.REMOTE) } bind RemoteProfilesSource::class

  single<PulledProfilesRepository> {
    PulledProfilesRepositoryImpl(
      localSource = get(named(DataSource.LOCAL)),
      remoteSource = get(named(DataSource.REMOTE)),
      profileScope = get(),
    )
  }
  single<ProfilesRepository> {
    ProfilesRepositoryImpl(
      pulledProfilesRepository = get(),
      localSource = get(named(DataSource.LOCAL)),
      sessionsRepository = get(),
      appScope = get(),
    )
  }

  includes(sdkProfileProfileScopeModule())
}

private fun sdkProfileProfileScopeModule() = module {
  scope<ProfileComponent> {
    scopedOf(::ProfileApiServiceImpl) bind ProfileApiService::class
    scopedOf(::NicknameApiServiceImpl) bind NicknameApiService::class

    scopedOf(::LocalProfileSourceImpl) { named(DataSource.LOCAL) } bind ProfileSource::class
    scopedOf(::RemoteProfileSourceImpl) { named(DataSource.REMOTE) } bind RemoteProfileSource::class

    scoped<ProfileRepository> {
      ProfileRepositoryImpl(
        profileId = get<ProfileComponent>().profileId,
        localSource = get(named(DataSource.LOCAL)),
        remoteSource = get(named(DataSource.REMOTE)),
        localProfilesSource = get(named(DataSource.LOCAL)),
        pulledProfilesRepository = get(),
        sessionRepository = get(),
        files = get(),
        compressor = get(),
        dispatchers = get(),
        profileScope = get<ProfileComponent>().coroutineScope,
      )
    }

    factoryOf(::ObserveProfileUseCaseImpl) bind ObserveProfileUseCase::class
    factoryOf(::SetAvatarUseCaseImpl) bind SetAvatarUseCase::class
    factoryOf(::DeleteAvatarUseCaseImpl) bind DeleteAvatarUseCase::class
    factoryOf(::SetNameUseCaseImpl) bind SetNameUseCase::class
    factoryOf(::SetDescriptionUseCaseImpl) bind SetDescriptionUseCase::class
    factoryOf(::CheckNicknameAvailabilityUseCaseImpl) bind CheckNicknameAvailabilityUseCase::class
    factoryOf(::SetNicknameUseCaseImpl) bind SetNicknameUseCase::class
    factoryOf(::RequestProfileDeletionUseCaseImpl) bind RequestProfileDeletionUseCase::class
    factoryOf(::CancelProfileDeletionUseCaseImpl) bind CancelProfileDeletionUseCase::class
  }
}
