package io.chefbook.sdk.network.impl.clients

import io.chefbook.libs.logger.Logger
import io.chefbook.sdk.network.impl.clients.plugins.RateLimiter
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.UserAgentConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ChefBookClientFactory {
  fun <T : HttpClientEngineConfig> create(
    engineFactory: HttpClientEngineFactory<T>,
    configureEngine: T.() -> Unit,
    configureUserAgent: UserAgentConfig.() -> Unit,
    isDevelop: Boolean,
  ) = HttpClient(engineFactory) {
    engine {
      configureEngine()
    }

    Logging {
      logger = object : io.ktor.client.plugins.logging.Logger {
        override fun log(message: String) =
          Logger.v { message.replace("%", "") }
      }
      level = if (isDevelop) LogLevel.BODY else LogLevel.NONE
    }

    install(UserAgent) {
      configureUserAgent()
    }

    install(RateLimiter)

    install(ContentNegotiation) {
      this.json(Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
      })
    }

    defaultRequest {
      url(if (isDevelop) DEVELOPMENT_HOST else PRODUCTION_HOST)
    }
  }
}
