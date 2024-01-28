package io.chefbook.sdk.network.impl.clients

import io.chefbook.sdk.auth.api.internal.data.repositories.TokensRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

class ChefBookClientFactory {

  fun create(
    isDevelop: Boolean,
    tokensRepository: TokensRepository?,
  ) = HttpClient(Darwin) {

    defaultRequest {
      url(if (isDevelop) DEVELOPMENT_HOST else PRODUCTION_HOST)
    }

    install(ContentNegotiation) {
      json()
    }

    tokensRepository?.let {
      Auth {
        bearer {
          loadTokens(tokensRepository::getTokens)
          refreshTokens(tokensRepository::refreshTokens)
        }
      }
    }

    Logging {
      level = if (isDevelop) LogLevel.ALL else LogLevel.NONE
    }
  }
}
