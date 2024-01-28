package io.chefbook.sdk.auth.impl.data.sources.remote.services.sessions

import io.chefbook.sdk.auth.impl.data.sources.remote.services.sessions.dto.SessionResponse
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse

internal interface SessionsApiService {
  suspend fun getSessions(): Result<List<SessionResponse>>
  suspend fun endSessions(sessionIds: List<Long>): Result<MessageResponse>
}
