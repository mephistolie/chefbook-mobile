package io.chefbook.sdk.network.impl.clients

import io.chefbook.sdk.auth.api.internal.data.repositories.TokensRepository
import io.chefbook.sdk.network.api.internal.clients.ProfileHttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import java.util.concurrent.ConcurrentHashMap

class ProfileHttpClientFactoryImpl(
  override val baseClient: HttpClient,
  private val tokensRepository: TokensRepository,
) : ProfileHttpClientFactory {

  private val clients: MutableMap<String, HttpClient> = ConcurrentHashMap()

  override fun getOrCreate(profileId: String): HttpClient {
    return clients.getOrPut(profileId) {
      baseClient.config {
        Auth {
          bearer {
            loadTokens { tokensRepository.getTokens(profileId) }
            refreshTokens(tokensRepository::refreshTokens)
          }
        }
      }
    }
  }

  override fun get(profileId: String): HttpClient? = clients[profileId]
}
