package io.chefbook.sdk.encryption.vault.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.libs.di.scopes.ProfileScope
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

  scope<ProfileScope> {

    scopedOf(::EncryptedVaultApiServiceImpl) bind EncryptedVaultApiService::class

    scoped<LocalEncryptedVaultSource>(named(DataSource.LOCAL)) { params ->
      getProperty<String>("")
      LocalEncryptedVaultSourceImpl(
        profileId = params[ProfileScope.PARAM_PROFILE_ID],
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
        scopes = get(),
      )
    }

    factory<CreateEncryptedVaultUseCase> { params ->
      CreateEncryptedVaultUseCaseImpl(
        profileId = params[ProfileScope.PARAM_PROFILE_ID],
        encryptionRepository = get(),
      )
    }

    factory<UnlockEncryptedVaultUseCase> { params ->
      UnlockEncryptedVaultUseCaseImpl(
        profileId = params[ProfileScope.PARAM_PROFILE_ID],
        encryptionRepository = get(),
      )
    }

    factoryOf(::ObserveEncryptedVaultStateUseCaseImpl) bind ObserveEncryptedVaultStateUseCase::class
    factoryOf(::GetEncryptedVaultStateUseCaseImpl) bind GetEncryptedVaultStateUseCase::class
    factoryOf(::LockEncryptedVaultUseCaseImpl) bind LockEncryptedVaultUseCase::class
    factoryOf(::DeleteEncryptedVaultUseCaseImpl) bind DeleteEncryptedVaultUseCase::class
  }
}
