package io.chefbook.sdk.auth.impl.di

import io.chefbook.libs.di.qualifiers.HttpClient
import io.chefbook.libs.di.scopes.ProfileComponent
import io.chefbook.sdk.auth.api.external.domain.usecases.ActivateProfileUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.ChangePasswordUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.ChooseLocalModeUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.ObserveCurrentProfileIdUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.ObserveProfileDeletionUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.RequestPasswordResetUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.ResetPasswordUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.RestoreProfileUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignInGoogleUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignInUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignOutUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignUpUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository
import io.chefbook.sdk.auth.api.internal.data.repositories.SessionRepository
import io.chefbook.sdk.auth.api.internal.data.repositories.SessionsRepository
import io.chefbook.sdk.auth.impl.data.repositories.PasswordRepository
import io.chefbook.sdk.auth.api.internal.data.repositories.TokensRepository
import io.chefbook.sdk.auth.impl.data.repositories.AuthRepositoryImpl
import io.chefbook.sdk.auth.impl.data.repositories.SessionRepositoryImpl
import io.chefbook.sdk.auth.impl.data.repositories.PasswordRepositoryImpl
import io.chefbook.sdk.auth.impl.data.repositories.SessionsRepositoryImpl
import io.chefbook.sdk.auth.impl.data.repositories.TokensRepositoryImpl
import io.chefbook.sdk.auth.impl.data.sources.local.SessionSource
import io.chefbook.sdk.auth.impl.data.sources.local.SessionSourceImpl
import io.chefbook.sdk.auth.impl.data.sources.local.SessionsSource
import io.chefbook.sdk.auth.impl.data.sources.local.SessionsSourceImpl
import io.chefbook.sdk.auth.impl.data.sources.local.datastore.SessionsInfoDataStore
import io.chefbook.sdk.auth.impl.data.sources.local.datastore.SessionsInfoDataStoreImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.AuthSourceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.AuthSource
import io.chefbook.sdk.auth.impl.data.sources.remote.SessionRefreshSource
import io.chefbook.sdk.auth.impl.data.sources.remote.SessionRefreshSourceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.PasswordSource
import io.chefbook.sdk.auth.impl.data.sources.remote.PasswordSourceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.AuthApiService
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.AuthApiServiceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.CurrentSessionApiService
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.CurrentSessionApiServiceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.PasswordApiService
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.PasswordApiServiceImpl
import io.chefbook.sdk.auth.impl.domain.usecases.ActivateProfileUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.ChangePasswordUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.ChooseLocalModeUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.ObserveCurrentProfileIdUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.ObserveProfileDeletionUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.RequestPasswordResetUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.ResetPasswordUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.RestoreProfileUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.SignInGoogleUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.SignInUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.SignOutUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.SignUpUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

fun sdkAuthModule() = module {

  singleOf(::SessionsInfoDataStoreImpl) bind SessionsInfoDataStore::class

  singleOf(::CurrentSessionApiServiceImpl) bind CurrentSessionApiService::class
  factory<AuthApiService> {
    AuthApiServiceImpl(
      client = get(named(HttpClient.BASE)),
    )
  }

  singleOf(::SessionsSourceImpl) bind SessionsSource::class
  singleOf(::SessionRefreshSourceImpl) bind SessionRefreshSource::class
  factoryOf(::AuthSourceImpl) bind AuthSource::class

  factoryOf(::SessionsRepositoryImpl) bind SessionsRepository::class
  factoryOf(::TokensRepositoryImpl) bind TokensRepository::class
  factoryOf(::AuthRepositoryImpl) bind AuthRepository::class

  factoryOf(::ObserveCurrentProfileIdUseCaseImpl) bind ObserveCurrentProfileIdUseCase::class
  factoryOf(::SignUpUseCaseImpl) bind SignUpUseCase::class
  factoryOf(::ActivateProfileUseCaseImpl) bind ActivateProfileUseCase::class
  factoryOf(::SignInUseCaseImpl) bind SignInUseCase::class
  factoryOf(::SignInGoogleUseCaseImpl) bind SignInGoogleUseCase::class
  factoryOf(::ChooseLocalModeUseCaseImpl) bind ChooseLocalModeUseCase::class
  factoryOf(::RequestPasswordResetUseCaseImpl) bind RequestPasswordResetUseCase::class
  factoryOf(::ResetPasswordUseCaseImpl) bind ResetPasswordUseCase::class
  factoryOf(::ChangePasswordUseCaseImpl) bind ChangePasswordUseCase::class
  factoryOf(::RestoreProfileUseCaseImpl) bind RestoreProfileUseCase::class

  scope<ProfileComponent> {
    scopedOf(::PasswordApiServiceImpl) bind PasswordApiService::class

    factoryOf(::PasswordSourceImpl) bind PasswordSource::class
    factory<SessionSource> {
      SessionSourceImpl(
        profileId = get<ProfileComponent>().profileId,
        dataStore = get(),
      )
    }

    factoryOf(::PasswordRepositoryImpl) bind PasswordRepository::class
    factoryOf(::SessionRepositoryImpl) bind SessionRepository::class
    factoryOf(::ObserveProfileDeletionUseCaseImpl) bind ObserveProfileDeletionUseCase::class

    factoryOf(::SignOutUseCaseImpl) bind SignOutUseCase::class
  }
}
