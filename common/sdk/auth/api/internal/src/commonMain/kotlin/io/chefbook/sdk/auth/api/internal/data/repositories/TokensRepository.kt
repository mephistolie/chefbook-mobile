package io.chefbook.sdk.auth.api.internal.data.repositories

import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams

interface TokensRepository {

  suspend fun getTokens(): BearerTokens?

  suspend fun getTokens(profileId: String): BearerTokens?

  suspend fun refreshTokens(params: RefreshTokensParams): BearerTokens?

  suspend fun sendWithoutRequest(): Boolean

  suspend fun sendWithoutRequest(profileId: String): Boolean
}
