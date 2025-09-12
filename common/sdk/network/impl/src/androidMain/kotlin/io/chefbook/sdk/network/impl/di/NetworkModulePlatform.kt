package io.chefbook.sdk.network.impl.di

import io.chefbook.sdk.network.api.internal.connection.ConnectivityRepository
import io.chefbook.sdk.network.impl.clients.ChefBookClientFactory
import io.chefbook.sdk.network.impl.clients.configuration.configureAndroidAgent
import io.chefbook.sdk.network.impl.clients.configuration.developTrustManager
import io.chefbook.sdk.network.impl.connection.ConnectivityRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpConfig
import org.koin.core.scope.Scope
import java.security.SecureRandom
import javax.net.ssl.SSLContext

actual fun Scope.createBaseClient(): HttpClient {
  val isDevelop = true
  //      when {
//        !BuildConfig.DEBUG -> false
//        else -> runBlocking { settingsRepository.getEnvironment() } == Environment.DEVELOP
//      }

  return ChefBookClientFactory.create<OkHttpConfig>(
    engineFactory = OkHttp,
    configureEngine = {
      config {
        if (isDevelop) {
          val sslContext = SSLContext.getInstance("SSL")
          sslContext.init(null, arrayOf(developTrustManager), SecureRandom())
          sslSocketFactory(sslContext.socketFactory, developTrustManager)
        }
      }
    },
    isDevelop = isDevelop,
    configureUserAgent = {
      configureAndroidAgent(
        context = get(),
      )
    },
  )
}

actual fun Scope.connectivityRepository(): ConnectivityRepository = ConnectivityRepositoryImpl(
  context = get(),
)
