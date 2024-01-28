package io.chefbook.sdk.auth.impl.data.sources.local

import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.Flow

internal interface TokensDataSource {
  fun observeTokens(): Flow<BearerTokens?>
  suspend fun getTokens(): BearerTokens?
  suspend fun updateTokens(tokens: BearerTokens)
  suspend fun clearTokens()
}
