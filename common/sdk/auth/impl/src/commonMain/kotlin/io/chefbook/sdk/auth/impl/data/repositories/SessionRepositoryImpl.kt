package io.chefbook.sdk.auth.impl.data.repositories

import io.chefbook.sdk.auth.api.internal.data.models.Session
import io.chefbook.sdk.auth.api.internal.data.repositories.SessionRepository
import io.chefbook.sdk.auth.impl.data.sources.local.SessionSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class SessionRepositoryImpl(
  private val localSource: SessionSource,
) : SessionRepository {

  override fun observeSession(): Flow<Session?> =
    localSource.observeSession()

  override fun observeSessionAlive(): Flow<Boolean> =
    observeSession().map { session ->
      session != null && session.profileDeletionTimestamp == null
    }

  override suspend fun isSessionAlive() =
    observeSessionAlive().first()

  override suspend fun isSessionOnline(): Boolean =
    localSource.getSession()?.isOnline == true

  override fun observeProfileDeletionTimestamp(): Flow<String?> =
    localSource.observeSession()
      .map { session -> session?.profileDeletionTimestamp }
      .distinctUntilChanged()
}
