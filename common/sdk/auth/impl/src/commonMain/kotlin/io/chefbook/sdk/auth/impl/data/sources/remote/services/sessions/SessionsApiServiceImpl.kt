package io.chefbook.sdk.auth.impl.data.sources.remote.services.sessions

import io.chefbook.sdk.auth.impl.data.sources.remote.services.sessions.dto.SessionResponse
import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url

internal class SessionsApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), SessionsApiService {

  override suspend fun getSessions(): Result<List<SessionResponse>> =
    safeGet(SESSIONS_ROUTE)

  override suspend fun endSessions(sessionIds: List<Long>): Result<MessageResponse> =
    safeDelete(SESSIONS_ROUTE) { setBody(sessionIds) }

  companion object {
    private const val SESSIONS_ROUTE = "/v1/auth/sessions"
  }
}
