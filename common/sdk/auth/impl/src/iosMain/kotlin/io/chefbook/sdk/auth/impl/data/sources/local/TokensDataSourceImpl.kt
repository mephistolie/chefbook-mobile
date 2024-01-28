package io.chefbook.sdk.auth.impl.data.sources.local

import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first

internal class TokensDataSourceImpl() : TokensDataSource {


  override fun observeTokens() =
    emptyFlow<BearerTokens?>()

  override suspend fun getTokens() =
    observeTokens().first()

  override suspend fun updateTokens(tokens: BearerTokens) = Unit

  override suspend fun clearTokens() = Unit
}
