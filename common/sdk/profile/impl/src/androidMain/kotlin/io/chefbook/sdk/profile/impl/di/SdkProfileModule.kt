package io.chefbook.sdk.profile.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.sdk.profile.api.external.domain.usecases.CheckNicknameAvailabilityUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.DeleteAvatarUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.SetAvatarUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.SetDescriptionUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.SetNameUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.SetNicknameUseCase
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository
import io.chefbook.sdk.profile.api.internal.data.sources.local.LocalProfileSource
import io.chefbook.sdk.profile.impl.data.repositories.ProfileRepositoryImpl
import io.chefbook.sdk.profile.impl.data.sources.local.LocalProfileSourceImpl
import io.chefbook.sdk.profile.impl.data.sources.remote.RemoteProfileSource
import io.chefbook.sdk.profile.impl.data.sources.remote.RemoteProfileSourceImpl
import io.chefbook.sdk.profile.impl.data.sources.remote.api.ProfileApiService
import io.chefbook.sdk.profile.impl.data.sources.remote.api.ProfileApiServiceImpl
import io.chefbook.sdk.profile.impl.data.sources.remote.nickname.NicknameApiService
import io.chefbook.sdk.profile.impl.data.sources.remote.nickname.NicknameApiServiceImpl
import io.chefbook.sdk.profile.impl.domain.usecases.CheckNicknameAvailabilityUseCaseImpl
import io.chefbook.sdk.profile.impl.domain.usecases.DeleteAvatarUseCaseImpl
import io.chefbook.sdk.profile.impl.domain.usecases.ObserveProfileUseCaseImpl
import io.chefbook.sdk.profile.impl.domain.usecases.SetAvatarUseCaseImpl
import io.chefbook.sdk.profile.impl.domain.usecases.SetDescriptionUseCaseImpl
import io.chefbook.sdk.profile.impl.domain.usecases.SetNameUseCaseImpl
import io.chefbook.sdk.profile.impl.domain.usecases.SetNicknameUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val sdkProfileModule = module {
  singleOf(::ProfileApiServiceImpl) bind ProfileApiService::class
  singleOf(::NicknameApiServiceImpl) bind NicknameApiService::class

  singleOf(::LocalProfileSourceImpl) { named(DataSource.LOCAL) } bind LocalProfileSource::class
  singleOf(::RemoteProfileSourceImpl) { named(DataSource.REMOTE) } bind RemoteProfileSource::class

  single<ProfileRepository> {
    ProfileRepositoryImpl(
      localSource = get(named(DataSource.LOCAL)),
      remoteSource = get(named(DataSource.REMOTE)),

      profileModeRepository = get(),
      files = get(),
      dispatchers = get(),
      scopes = get(),
    )
  }

  factoryOf(::ObserveProfileUseCaseImpl) bind ObserveProfileUseCase::class
  factoryOf(::SetAvatarUseCaseImpl) bind SetAvatarUseCase::class
  factoryOf(::DeleteAvatarUseCaseImpl) bind DeleteAvatarUseCase::class
  factoryOf(::SetNameUseCaseImpl) bind SetNameUseCase::class
  factoryOf(::SetDescriptionUseCaseImpl) bind SetDescriptionUseCase::class
  factoryOf(::CheckNicknameAvailabilityUseCaseImpl) bind CheckNicknameAvailabilityUseCase::class
  factoryOf(::SetNicknameUseCaseImpl) bind SetNicknameUseCase::class
}
