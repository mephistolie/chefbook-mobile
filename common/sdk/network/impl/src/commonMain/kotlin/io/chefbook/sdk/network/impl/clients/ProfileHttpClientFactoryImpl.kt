package io.chefbook.sdk.network.impl.clients

import io.chefbook.sdk.auth.api.internal.data.repositories.TokensRepository
import io.chefbook.sdk.network.api.internal.clients.ProfileHttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet

class ProfileHttpClientFactoryImpl(
  override val baseClient: HttpClient,
  private val tokensRepository: TokensRepository,
) : ProfileHttpClientFactory {

  private val clients: MutableStateFlow<Map<String, HttpClient>> = MutableStateFlow(emptyMap())

  override fun getOrCreate(profileId: String): HttpClient =
    clients.updateAndGet { clients ->
      if (!clients.containsKey(profileId)) {
        clients + (profileId to baseClient.config {
          Auth {
            bearer {
              loadTokens { tokensRepository.getTokens(profileId) }
              refreshTokens(tokensRepository::refreshTokens)
            }
          }
        })
      } else {
        clients
      }
    }.getValue(profileId)

  override fun get(profileId: String): HttpClient? =
    clients.value[profileId]
}
