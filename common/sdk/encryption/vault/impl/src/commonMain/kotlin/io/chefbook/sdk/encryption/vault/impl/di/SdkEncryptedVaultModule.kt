package io.chefbook.sdk.encryption.vault.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.libs.di.scopes.ProfileComponent
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.CreateEncryptedVaultUseCase
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.DeleteEncryptedVaultUseCase
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.GetEncryptedVaultStateUseCase
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.LockEncryptedVaultUseCase
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.ObserveEncryptedVaultStateUseCase
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.UnlockEncryptedVaultUseCase
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository
import io.chefbook.sdk.encryption.vault.impl.data.repositories.EncryptedVaultRepositoryImpl
import io.chefbook.sdk.encryption.vault.impl.data.sources.local.LocalEncryptedVaultSource
import io.chefbook.sdk.encryption.vault.impl.data.sources.local.LocalEncryptedVaultSourceImpl
import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.RemoteEncryptedVaultSource
import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.RemoteEncryptedVaultSourceImpl
import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services.EncryptedVaultApiService
import io.chefbook.sdk.encryption.vault.impl.data.sources.remote.services.EncryptedVaultApiServiceImpl
import io.chefbook.sdk.encryption.vault.impl.domain.usecases.CreateEncryptedVaultUseCaseImpl
import io.chefbook.sdk.encryption.vault.impl.domain.usecases.DeleteEncryptedVaultUseCaseImpl
import io.chefbook.sdk.encryption.vault.impl.domain.usecases.GetEncryptedVaultStateUseCaseImpl
import io.chefbook.sdk.encryption.vault.impl.domain.usecases.LockEncryptedVaultUseCaseImpl
import io.chefbook.sdk.encryption.vault.impl.domain.usecases.ObserveEncryptedVaultStateUseCaseImpl
import io.chefbook.sdk.encryption.vault.impl.domain.usecases.UnlockEncryptedVaultUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

fun sdkEncryptedVaultModule() = module {

  scope<ProfileComponent> {

    scopedOf(::EncryptedVaultApiServiceImpl) bind EncryptedVaultApiService::class

    scoped<LocalEncryptedVaultSource>(named(DataSource.LOCAL)) {
      getProperty<String>("")
      LocalEncryptedVaultSourceImpl(
        profileId = get<ProfileComponent>().profileId,
        io = get(),
        dispatchers = get(),
      )
    }

    scoped<RemoteEncryptedVaultSource>(named(DataSource.REMOTE)) { params ->
      RemoteEncryptedVaultSourceImpl(
        api = get(),
      )
    }

    scoped<EncryptedVaultRepository> {
      EncryptedVaultRepositoryImpl(
        localSource = get(named(DataSource.LOCAL)),
        remoteSource = get(named(DataSource.REMOTE)),
        sources = get(),
        dispatchers = get(),
        profileScope = get<ProfileComponent>().coroutineScope,
      )
    }

    factory<CreateEncryptedVaultUseCase> {
      CreateEncryptedVaultUseCaseImpl(
        profileId = get<ProfileComponent>().profileId,
        encryptionRepository = get(),
      )
    }

    factory<UnlockEncryptedVaultUseCase> {
      UnlockEncryptedVaultUseCaseImpl(
        profileId = get<ProfileComponent>().profileId,
        encryptionRepository = get(),
      )
    }

    factoryOf(::ObserveEncryptedVaultStateUseCaseImpl) bind ObserveEncryptedVaultStateUseCase::class
    factoryOf(::GetEncryptedVaultStateUseCaseImpl) bind GetEncryptedVaultStateUseCase::class
    factoryOf(::LockEncryptedVaultUseCaseImpl) bind LockEncryptedVaultUseCase::class
    factoryOf(::DeleteEncryptedVaultUseCaseImpl) bind DeleteEncryptedVaultUseCase::class
  }
}
