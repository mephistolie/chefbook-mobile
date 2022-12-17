package com.mysty.chefbook.api.auth.data.di

import com.mysty.chefbook.api.auth.data.IAuthSource
import com.mysty.chefbook.api.auth.data.remote.RemoteAuthSource
import com.mysty.chefbook.api.auth.data.remote.api.AuthApi
import com.mysty.chefbook.api.auth.data.repositories.AuthRepo
import com.mysty.chefbook.api.auth.data.repositories.TokensRepo
import com.mysty.chefbook.api.auth.domain.IAuthRepo
import com.mysty.chefbook.api.auth.domain.ITokensRepo
import com.mysty.chefbook.api.auth.domain.usecases.ChooseLocalModeUseCase
import com.mysty.chefbook.api.auth.domain.usecases.IChooseLocalModeUseCase
import com.mysty.chefbook.api.auth.domain.usecases.IRefreshSessionUseCase
import com.mysty.chefbook.api.auth.domain.usecases.ISignInUseCase
import com.mysty.chefbook.api.auth.domain.usecases.ISignUpUseCase
import com.mysty.chefbook.api.auth.domain.usecases.RefreshSessionUseCase
import com.mysty.chefbook.api.auth.domain.usecases.SignInUseCase
import com.mysty.chefbook.api.auth.domain.usecases.SignUpUseCase
import com.mysty.chefbook.api.recipe.data.local.DataStoreUtils
import com.mysty.chefbook.core.di.Qualifiers
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val apiAuthModule = module {

    singleOf(DataStoreUtils::getTokensDataStore) { named(Qualifiers.DataStore.TOKENS) }

    single { createAuthApi(get(named(Qualifiers.ANONYMOUS))) }

    singleOf(::RemoteAuthSource) { named(Qualifiers.REMOTE) } bind IAuthSource::class

    single<IAuthRepo> { AuthRepo(get(named(Qualifiers.REMOTE))) }
    single<ITokensRepo> { TokensRepo(get(named(Qualifiers.DataStore.TOKENS)), get()) }

    factoryOf(::SignUpUseCase) bind ISignUpUseCase::class
    factoryOf(::SignInUseCase) bind ISignInUseCase::class
    factoryOf(::ChooseLocalModeUseCase) bind IChooseLocalModeUseCase::class
    factoryOf(::RefreshSessionUseCase) bind IRefreshSessionUseCase::class

}

private fun createAuthApi(retrofit: Retrofit) = retrofit.create(AuthApi::class.java)