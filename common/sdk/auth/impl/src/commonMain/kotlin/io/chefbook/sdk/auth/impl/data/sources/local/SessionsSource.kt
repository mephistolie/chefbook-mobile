package io.chefbook.sdk.auth.impl.data.sources.local

import io.chefbook.sdk.auth.api.internal.data.models.Session
import kotlinx.coroutines.flow.Flow

interface SessionsSource {

  fun observeSessionsInfo(): Flow<Map<String, Session>>

  suspend fun getSessionsInfo(): Map<String, Session>

  suspend fun getCurrentSessionInfo(): Session?

  fun observeCurrentProfileId(): Flow<String?>

  suspend fun saveSession(session: Session, current: Boolean = false)

  suspend fun setCurrentProfile(profileId: String?)

  suspend fun endSession(profileId: String): String?

  suspend fun endSessionByRefreshToken(refreshToken: String)
}
