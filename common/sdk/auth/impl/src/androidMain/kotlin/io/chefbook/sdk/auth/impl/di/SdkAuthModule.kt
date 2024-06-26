package io.chefbook.sdk.auth.impl.di

import io.chefbook.sdk.auth.api.external.domain.usecases.ActivateProfileUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.ChangePasswordUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.ChooseLocalModeUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.ObserveProfileDeletionUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.RequestPasswordResetUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.ResetPasswordUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.RestoreProfileUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignInGoogleUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignInUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignOutUseCase
import io.chefbook.sdk.auth.api.external.domain.usecases.SignUpUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository
import io.chefbook.sdk.auth.api.internal.data.repositories.CurrentSessionRepository
import io.chefbook.sdk.auth.api.internal.data.repositories.PasswordRepository
import io.chefbook.sdk.auth.api.internal.data.repositories.TokensRepository
import io.chefbook.sdk.auth.impl.data.repositories.AuthRepositoryImpl
import io.chefbook.sdk.auth.impl.data.repositories.CurrentSessionRepositoryImpl
import io.chefbook.sdk.auth.impl.data.repositories.PasswordRepositoryImpl
import io.chefbook.sdk.auth.impl.data.repositories.TokensRepositoryImpl
import io.chefbook.sdk.auth.impl.data.sources.local.CurrentSessionLocalDataSource
import io.chefbook.sdk.auth.impl.data.sources.local.CurrentSessionLocalDataSourceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.AuthDataDataSourceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.AuthDataSource
import io.chefbook.sdk.auth.impl.data.sources.remote.CurrentSessionRemoteDataSource
import io.chefbook.sdk.auth.impl.data.sources.remote.CurrentSessionRemoteDataSourceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.PasswordDataSource
import io.chefbook.sdk.auth.impl.data.sources.remote.PasswordDataSourceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.AuthApiService
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.AuthApiServiceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.CurrentSessionApiService
import io.chefbook.sdk.auth.impl.data.sources.remote.services.auth.CurrentSessionApiServiceImpl
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.PasswordApiService
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.PasswordApiServiceImpl
import io.chefbook.sdk.auth.impl.domain.usecases.ActivateProfileUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.ChangePasswordUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.ChooseLocalModeUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.ObserveProfileDeletionUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.RequestPasswordResetUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.ResetPasswordUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.RestoreProfileUseCaseImpl
import io.chefbook.sdk.auth.impl.domain.usecases.SignInGoogleUseCaseImpl
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
  singleOf(::PasswordApiServiceImpl) bind PasswordApiService::class
  singleOf(::CurrentSessionApiServiceImpl) bind CurrentSessionApiService::class

  singleOf(::CurrentSessionLocalDataSourceImpl) bind CurrentSessionLocalDataSource::class

  factoryOf(::AuthDataDataSourceImpl) bind AuthDataSource::class
  factoryOf(::PasswordDataSourceImpl) bind PasswordDataSource::class
  factoryOf(::CurrentSessionRemoteDataSourceImpl) bind CurrentSessionRemoteDataSource::class

  single<TokensRepository> {
    TokensRepositoryImpl(
      localSource = get(),
      remoteSource = get(),
    )
  }
  singleOf(::AuthRepositoryImpl) { createdAtStart() } bind AuthRepository::class
  factoryOf(::PasswordRepositoryImpl) bind PasswordRepository::class
  factoryOf(::CurrentSessionRepositoryImpl) bind CurrentSessionRepository::class

  factoryOf(::SignUpUseCaseImpl) bind SignUpUseCase::class
  factoryOf(::ActivateProfileUseCaseImpl) bind ActivateProfileUseCase::class
  factoryOf(::SignInUseCaseImpl) bind SignInUseCase::class
  factoryOf(::SignInGoogleUseCaseImpl) bind SignInGoogleUseCase::class
  factoryOf(::SignOutUseCaseImpl) bind SignOutUseCase::class
  factoryOf(::ChooseLocalModeUseCaseImpl) bind ChooseLocalModeUseCase::class
  factoryOf(::RequestPasswordResetUseCaseImpl) bind RequestPasswordResetUseCase::class
  factoryOf(::ResetPasswordUseCaseImpl) bind ResetPasswordUseCase::class
  factoryOf(::ChangePasswordUseCaseImpl) bind ChangePasswordUseCase::class
  factoryOf(::ObserveProfileDeletionUseCaseImpl) bind ObserveProfileDeletionUseCase::class
  factoryOf(::RestoreProfileUseCaseImpl) bind RestoreProfileUseCase::class
}
