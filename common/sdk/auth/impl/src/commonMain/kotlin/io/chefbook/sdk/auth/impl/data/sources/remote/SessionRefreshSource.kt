package io.chefbook.sdk.auth.impl.data.sources.remote

import io.chefbook.sdk.auth.api.internal.data.models.Session
import io.ktor.client.HttpClient

internal interface SessionRefreshSource {

  suspend fun refreshSession(client: HttpClient, refreshToken: String): Result<Session>
}
