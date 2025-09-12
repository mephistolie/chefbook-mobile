package io.chefbook.sdk.network.impl.di

import io.chefbook.sdk.network.api.internal.connection.ConnectivityRepository
import io.chefbook.sdk.network.impl.clients.ChefBookClientFactory
import io.chefbook.sdk.network.impl.clients.configuration.configureIosAgent
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.engine.darwin.DarwinClientEngineConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.scope.Scope

actual fun Scope.createBaseClient(): HttpClient {
  val isDevelop = true
  //      when {
//        !BuildConfig.DEBUG -> false
//        else -> runBlocking { settingsRepository.getEnvironment() } == Environment.DEVELOP
//      }

  return ChefBookClientFactory.create<DarwinClientEngineConfig>(
    engineFactory = Darwin,
    configureEngine = {},
    isDevelop = isDevelop,
    configureUserAgent = { configureIosAgent() },
  )
}

// TODO
actual fun Scope.connectivityRepository(): ConnectivityRepository =
  object : ConnectivityRepository {
    override fun observeConnectivity(): Flow<Boolean> = flowOf(true)

    override suspend fun hasActiveConnection(): Boolean = true
  }