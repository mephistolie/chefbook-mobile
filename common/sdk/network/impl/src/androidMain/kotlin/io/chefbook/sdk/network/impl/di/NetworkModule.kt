package io.chefbook.sdk.network.impl.di

import android.content.Context
import io.chefbook.libs.di.scopes.ProfileComponent
import io.chefbook.sdk.network.api.internal.clients.ProfileHttpClientFactory
import io.chefbook.sdk.network.api.internal.connection.ConnectivityRepository
import io.chefbook.sdk.network.impl.clients.ChefBookClientFactory
import io.chefbook.sdk.network.impl.clients.ProfileHttpClientFactoryImpl
import io.chefbook.sdk.network.impl.clients.interceptors.EncryptedImageInterceptor
import io.chefbook.sdk.network.impl.clients.interceptors.RateLimitInterceptor
import io.chefbook.sdk.network.impl.clients.okHttpClient
import io.chefbook.sdk.network.impl.connection.ConnectivityRepositoryImpl
import io.chefbook.libs.di.qualifiers.HttpClient
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

fun sdkNetworkModule() = module {
  factoryOf(::EncryptedImageInterceptor)

  singleOf(::baseClient) { qualifier = named(HttpClient.BASE) }
  singleOf(::imageClient) { qualifier = named(HttpClient.ENCRYPTED_IMAGE) }

  single<ProfileHttpClientFactory> {
    ProfileHttpClientFactoryImpl(
      baseClient = get(named(HttpClient.BASE)),
      tokensRepository = get(),
    )
  }

  singleOf(::ConnectivityRepositoryImpl) bind ConnectivityRepository::class

  scope<ProfileComponent> {
    scoped {
      get<ProfileHttpClientFactory>().getOrCreate(get<ProfileComponent>().profileId)
    }
  }
}

private fun baseClient(
  context: Context,
  settingsRepository: SettingsRepository,
) =
  ChefBookClientFactory(context = context).create(
    isDevelop = true,
//    when {
//      !BuildConfig.DEBUG -> false
//      else -> runBlocking { settingsRepository.getEnvironment() } == Environment.DEVELOP
//    },
    interceptors = listOf(RateLimitInterceptor),
  )

private fun imageClient(encryptedImageInterceptor: EncryptedImageInterceptor): OkHttpClient =
  okHttpClient(interceptors = listOf(encryptedImageInterceptor))
