package io.chefbook.sdk.auth.impl.data.sources.local

import io.chefbook.sdk.auth.api.internal.data.models.Session
import kotlinx.coroutines.flow.Flow

interface SessionSource {

  fun observeSession(): Flow<Session?>

  suspend fun getSession(): Session?

  suspend fun isSessionOnline(): Boolean
}
