package io.chefbook.sdk.auth.impl.data.sources.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BearerTokens

internal interface CurrentSessionDataSource {

  suspend fun refreshSession(client: HttpClient, refreshToken: String): Result<BearerTokens>
}
