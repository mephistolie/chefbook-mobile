package com.mysty.chefbook.api.profile.data.di

import com.mysty.chefbook.api.profile.data.ILocalProfileSource
import com.mysty.chefbook.api.profile.data.IRemoteProfileSource
import com.mysty.chefbook.api.profile.data.local.LocalProfileSource
import com.mysty.chefbook.api.profile.data.remote.RemoteProfileSource
import com.mysty.chefbook.api.profile.data.remote.api.ProfileApi
import com.mysty.chefbook.api.profile.data.repositories.ProfileRepo
import com.mysty.chefbook.api.profile.domain.IProfileRepo
import com.mysty.chefbook.api.profile.domain.usecases.IObserveProfileUseCase
import com.mysty.chefbook.api.profile.domain.usecases.ObserveProfileUseCase
import com.mysty.chefbook.api.recipe.data.local.DataStoreUtils
import com.mysty.chefbook.core.di.Qualifiers
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val apiProfileModule = module {

    singleOf(DataStoreUtils::getProfileCacheDataStore) { named(Qualifiers.DataStore.PROFILE) }

    single { createProfileApi(get(named(Qualifiers.AUTHORIZED))) }

    single(named(Qualifiers.LOCAL)) { LocalProfileSource(get(named(Qualifiers.DataStore.PROFILE))) } bind ILocalProfileSource::class
    singleOf(::RemoteProfileSource) { named(Qualifiers.REMOTE) } bind IRemoteProfileSource::class

    single<IProfileRepo> { ProfileRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(), get(), get()) }

    factoryOf(::ObserveProfileUseCase) bind IObserveProfileUseCase::class

}

private fun createProfileApi(retrofit: Retrofit) = retrofit.create(ProfileApi::class.java)
