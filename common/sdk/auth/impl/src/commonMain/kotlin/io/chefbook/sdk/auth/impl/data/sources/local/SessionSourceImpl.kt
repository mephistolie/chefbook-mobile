package io.chefbook.sdk.auth.impl.data.sources.local

import io.chefbook.sdk.auth.api.internal.data.models.Session
import io.chefbook.sdk.auth.impl.data.sources.common.dto.deserialize
import io.chefbook.sdk.auth.impl.data.sources.local.datastore.SessionsInfoDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class SessionSourceImpl(
  private val profileId: String,
  private val dataStore: SessionsInfoDataStore,
) : SessionSource {

  override fun observeSession() = dataStore.data.map { sessionsInfo ->
    sessionsInfo.sessions[profileId]?.deserialize()
  }

  override suspend fun getSession(): Session? =
    observeSession().first()

  override suspend fun isSessionOnline(): Boolean =
    getSession()?.isOnline == true
}
