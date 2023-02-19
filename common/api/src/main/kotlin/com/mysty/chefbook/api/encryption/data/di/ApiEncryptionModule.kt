package com.mysty.chefbook.api.encryption.data.di

import com.mysty.chefbook.api.encryption.data.ILocalEncryptionSource
import com.mysty.chefbook.api.encryption.data.IRemoteEncryptionSource
import com.mysty.chefbook.api.encryption.data.local.LocalEncryptionSource
import com.mysty.chefbook.api.encryption.data.remote.RemoteEncryptionSource
import com.mysty.chefbook.api.encryption.data.remote.api.EncryptionApi
import com.mysty.chefbook.api.encryption.data.repositories.EncryptedVaultRepo
import com.mysty.chefbook.api.encryption.domain.IEncryptedVaultRepo
import com.mysty.chefbook.api.encryption.domain.usecases.CreateEncryptedVaultUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.DeleteEncryptedVaultUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.GetEncryptedVaultStateUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.ICreateEncryptedVaultUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.IDeleteEncryptedVaultUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.IGetEncryptedVaultStateUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.ILockEncryptedVaultUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.IObserveEncryptedVaultStateUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.IUnlockEncryptedVaultUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.LockEncryptedVaultUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.ObserveEncryptedVaultStateUseCase
import com.mysty.chefbook.api.encryption.domain.usecases.UnlockEncryptedVaultUseCase
import com.mysty.chefbook.core.di.Qualifiers
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val apiEncryptionModule = module {

    single { createEncryptionApi(get(named(Qualifiers.AUTHORIZED))) }

    singleOf(::LocalEncryptionSource) { named(Qualifiers.LOCAL) } bind ILocalEncryptionSource::class
    single(named(Qualifiers.REMOTE)) { RemoteEncryptionSource(get(), get(named(Qualifiers.REMOTE)), get()) } bind IRemoteEncryptionSource::class

    single<IEncryptedVaultRepo> { EncryptedVaultRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(), get(), get()) }

    factoryOf(::ObserveEncryptedVaultStateUseCase) bind IObserveEncryptedVaultStateUseCase::class
    factoryOf(::GetEncryptedVaultStateUseCase) bind IGetEncryptedVaultStateUseCase::class
    factoryOf(::CreateEncryptedVaultUseCase) bind ICreateEncryptedVaultUseCase::class
    factoryOf(::UnlockEncryptedVaultUseCase) bind IUnlockEncryptedVaultUseCase::class
    factoryOf(::LockEncryptedVaultUseCase) bind ILockEncryptedVaultUseCase::class
    factoryOf(::DeleteEncryptedVaultUseCase) bind IDeleteEncryptedVaultUseCase::class

}

private fun createEncryptionApi(retrofit: Retrofit) = retrofit.create(EncryptionApi::class.java)
