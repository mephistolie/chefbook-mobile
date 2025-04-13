package io.chefbook.sdk.auth.api.internal.data.repositories

import io.chefbook.sdk.auth.api.internal.data.models.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

  fun observeSession(): Flow<Session?>

  fun observeSessionAlive(): Flow<Boolean>

  suspend fun isSessionAlive(): Boolean

  suspend fun isSessionOnline(): Boolean

  fun observeProfileDeletionTimestamp(): Flow<String?>
}
