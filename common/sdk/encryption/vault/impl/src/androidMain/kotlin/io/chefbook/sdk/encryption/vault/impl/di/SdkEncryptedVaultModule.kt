package io.chefbook.sdk.encryption.vault.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
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
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val sdkEncryptedVaultModule = module {

  singleOf(::EncryptedVaultApiServiceImpl) bind EncryptedVaultApiService::class

  singleOf(::LocalEncryptedVaultSourceImpl) { named(DataSource.LOCAL) } bind LocalEncryptedVaultSource::class
  singleOf(::RemoteEncryptedVaultSourceImpl) { named(DataSource.REMOTE) } bind RemoteEncryptedVaultSource::class

  single<EncryptedVaultRepository> {
    EncryptedVaultRepositoryImpl(
      get(named(DataSource.LOCAL)),
      get(named(DataSource.REMOTE)),
      get(),
      get(),
      get(),
    )
  }

  factoryOf(::ObserveEncryptedVaultStateUseCaseImpl) bind ObserveEncryptedVaultStateUseCase::class
  factoryOf(::GetEncryptedVaultStateUseCaseImpl) bind GetEncryptedVaultStateUseCase::class
  factoryOf(::CreateEncryptedVaultUseCaseImpl) bind CreateEncryptedVaultUseCase::class
  factoryOf(::UnlockEncryptedVaultUseCaseImpl) bind UnlockEncryptedVaultUseCase::class
  factoryOf(::LockEncryptedVaultUseCaseImpl) bind LockEncryptedVaultUseCase::class
  factoryOf(::DeleteEncryptedVaultUseCaseImpl) bind DeleteEncryptedVaultUseCase::class
}
