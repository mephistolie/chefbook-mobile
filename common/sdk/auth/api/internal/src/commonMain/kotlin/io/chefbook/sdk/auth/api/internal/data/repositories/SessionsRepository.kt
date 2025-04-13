package io.chefbook.sdk.auth.api.internal.data.repositories

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.auth.api.internal.data.models.Session
import kotlinx.coroutines.flow.Flow

interface SessionsRepository {

  fun observeSessions(): Flow<Map<String, Session>>

  suspend fun refreshTokens(): EmptyResult

  fun clearClientTokens(profileId: String)
}
