package io.chefbook.sdk.auth.impl.data.sources.local

import io.chefbook.libs.logger.Logger
import io.chefbook.sdk.auth.api.internal.data.models.Session
import io.chefbook.sdk.auth.impl.data.sources.common.dto.deserialize
import io.chefbook.sdk.auth.impl.data.sources.common.dto.serialize
import io.chefbook.sdk.auth.impl.data.sources.local.datastore.SessionsInfoDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class SessionsSourceImpl(
  private val dataStore: SessionsInfoDataStore,
) : SessionsSource {

  override fun observeSessionsInfo(): Flow<Map<String, Session>> =
    dataStore.data.map { sessionsInfo ->
      sessionsInfo.sessions.mapValues { (_, session) -> session.deserialize() }
    }

  override suspend fun getSessionsInfo(): Map<String, Session> =
    observeSessionsInfo().first()

  override suspend fun getCurrentSessionInfo(): Session? {
    val sessions = dataStore.data.first()
    return sessions.sessions[sessions.currentProfileId]?.deserialize()
  }

  override fun observeCurrentProfileId(): Flow<String?> =
    dataStore.data.map { it.currentProfileId }

  override suspend fun saveSession(
    session: Session,
    current: Boolean,
  ) {
    val rawSession = session.serialize()
    dataStore.updateData { sessionsInfo ->
      sessionsInfo.copy(
        sessions = sessionsInfo.sessions + (session.profileId to rawSession),
        currentProfileId = if (current) session.profileId else sessionsInfo.currentProfileId,
      )
    }
    Logger.i("Tokens updated for profile ${session.profileId}")
  }

  override suspend fun setCurrentProfile(profileId: String?) {
    dataStore.updateData { sessionsInfo ->
      sessionsInfo.copy(currentProfileId = profileId)
    }
    Logger.i("Switch current profile to $profileId")
  }

  override suspend fun endSession(profileId: String): String? {
    var refreshToken: String? = null
    dataStore.updateData { sessionsInfo ->
      sessionsInfo.copy(
        sessions = sessionsInfo.sessions.filter { (_, session) ->
          val removed = session.profileId != profileId
          if (removed) refreshToken = session.refreshToken
          !removed
        },
        currentProfileId = if (sessionsInfo.currentProfileId == profileId) {
          null
        } else {
          sessionsInfo.currentProfileId
        },
      )
    }
    Logger.i("Tokens cleared for profile $profileId")
    return refreshToken
  }

  override suspend fun endSessionByRefreshToken(refreshToken: String) {
    val profileId = getSessionsInfo().values
      .find { it.refreshToken == refreshToken }?.profileId ?: return
    endSession(profileId)
  }
}
