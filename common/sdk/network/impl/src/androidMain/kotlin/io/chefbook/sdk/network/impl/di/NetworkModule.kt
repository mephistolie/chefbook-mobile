package io.chefbook.sdk.network.impl.di

import android.content.Context
import io.chefbook.sdk.auth.api.internal.data.repositories.TokensRepository
import io.chefbook.sdk.network.api.internal.connection.ConnectivityRepository
import io.chefbook.sdk.network.impl.BuildConfig
import io.chefbook.sdk.network.impl.clients.ChefBookClientFactory
import io.chefbook.sdk.network.impl.clients.interceptors.EncryptedImageInterceptor
import io.chefbook.sdk.network.impl.clients.interceptors.RateLimitInterceptor
import io.chefbook.sdk.network.impl.clients.okHttpClient
import io.chefbook.sdk.network.impl.connection.ConnectivityRepositoryImpl
import io.chefbook.sdk.network.impl.di.qualifiers.HttpClient
import io.chefbook.sdk.settings.api.external.domain.entities.Environment
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sdkNetworkModule = module {
  factoryOf(::EncryptedImageInterceptor)

  singleOf(::authorizedChefBookClient)
  singleOf(::imageClient) { named(HttpClient.ENCRYPTED_IMAGE) }

  singleOf(::ConnectivityRepositoryImpl) bind ConnectivityRepository::class
}

private fun imageClient(encryptedImageInterceptor: EncryptedImageInterceptor): OkHttpClient =
  okHttpClient(interceptors = listOf(encryptedImageInterceptor))

private fun authorizedChefBookClient(
  context: Context,
  tokensRepository: TokensRepository,
  settingsRepository: SettingsRepository,
) =
  ChefBookClientFactory(context = context).create(
    isDevelop = when {
      !BuildConfig.DEBUG -> false
      else -> runBlocking { settingsRepository.getEnvironment() } == Environment.DEVELOP
    },
    tokensRepository = tokensRepository,
    interceptors = listOf(RateLimitInterceptor),
  )
