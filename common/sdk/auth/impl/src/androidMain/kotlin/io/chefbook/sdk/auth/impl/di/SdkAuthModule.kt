package io.chefbook.sdk.auth.impl.di

import io.chefbook.sdk.auth.api.external.domain.usecases.ChooseLocalModeUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignInUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignOutUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignUpUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository
import io.chefbook.sdk.auth.api.internal.data.repositories.CurrentSessionRepository
import io.chefbook.sdk.auth.api.internal.data.repositories.TokensRepository
import io.chefbook.sdk.auth.impl.data.repositories.AuthRepositoryImpl
import io.chefbook.sdk.auth.impl.data.repositories.CurrentSessionRepositoryImpl
import io.chefbook.sdk.auth.impl.data.repositories.TokensRepositoryImpl
import io.chefbook.sdk.auth.impl.data.sources.local.TokensDataSource
import io.chefbook.sdk.auth.impl.data.sources.local.TokensDataSourceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.AuthDataDataSourceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.AuthDataSource
import io.chefbook.sdk.auth.impl.data.sources.remote.CurrentSessionDataSource
import io.chefbook.sdk.auth.impl.data.sources.remote.CurrentSessionDataSourceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.AuthApiService
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.AuthApiServiceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.CurrentSessionApiService
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.CurrentSessionApiServiceImpl
import io.chefbook.sdk.auth.impl.domain.usecases.ChooseLocalModeUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.SignInUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.SignOutUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.SignUpUseCaseImpl
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sdkAuthModule = module {
  singleOf(::AuthApiServiceImpl) bind AuthApiService::class
  singleOf(::CurrentSessionApiServiceImpl) bind CurrentSessionApiService::class

  singleOf(::TokensDataSourceImpl) bind TokensDataSource::class

  factoryOf(::AuthDataDataSourceImpl) bind AuthDataSource::class
  factoryOf(::CurrentSessionDataSourceImpl) bind CurrentSessionDataSource::class

  single<TokensRepository> {
    TokensRepositoryImpl(
      tokensDataSource = get(),
      currentSessionDataSource = get(),
    )
  }
  singleOf(::AuthRepositoryImpl) { createdAtStart() } bind AuthRepository::class
  factoryOf(::CurrentSessionRepositoryImpl) bind CurrentSessionRepository::class

  factoryOf(::SignUpUseCaseImpl) bind SignUpUseCase::class
  factoryOf(::SignInUseCaseImpl) bind SignInUseCase::class
  factoryOf(::SignOutUseCaseImpl) bind SignOutUseCase::class
  factoryOf(::ChooseLocalModeUseCaseImpl) bind ChooseLocalModeUseCase::class
}
