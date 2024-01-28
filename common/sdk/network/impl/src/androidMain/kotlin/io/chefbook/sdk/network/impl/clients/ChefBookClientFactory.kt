package io.chefbook.sdk.network.impl.clients

import android.content.Context
import io.chefbook.sdk.auth.api.internal.data.repositories.TokensRepository
import io.chefbook.sdk.network.impl.clients.configuration.developTrustManager
import io.chefbook.sdk.network.impl.clients.configuration.installUserAgent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import java.security.SecureRandom
import javax.net.ssl.SSLContext

class ChefBookClientFactory(
  private val context: Context,
) {
  fun create(
    isDevelop: Boolean,
    tokensRepository: TokensRepository?,
    interceptors: List<Interceptor> = emptyList(),
  ) = HttpClient(OkHttp) {
    engine {
      config {
        if (isDevelop) {
          val sslContext = SSLContext.getInstance("SSL")
          sslContext.init(null, arrayOf(developTrustManager), SecureRandom())
          sslSocketFactory(sslContext.socketFactory, developTrustManager)
        }
      }

      interceptors.forEach(this::addInterceptor)
    }

    Logging {
      logger = object : Logger {
        override fun log(message: String) =
          io.chefbook.libs.logger.Logger.v(message.replace("%", ""))
      }
      level = if (isDevelop) LogLevel.BODY else LogLevel.NONE
    }

    installUserAgent(context)

    tokensRepository?.let {
      Auth {
        bearer {
          loadTokens(tokensRepository::getTokens)
          refreshTokens(tokensRepository::refreshTokens)
        }
      }
    }

    install(ContentNegotiation) {
      json(Json { ignoreUnknownKeys = true })
    }

    defaultRequest {
      url(if (isDevelop) DEVELOPMENT_HOST else PRODUCTION_HOST)
    }
  }
}

