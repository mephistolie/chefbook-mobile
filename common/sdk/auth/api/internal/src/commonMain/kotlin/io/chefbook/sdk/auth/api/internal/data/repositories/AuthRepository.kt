package io.chefbook.sdk.auth.api.internal.data.repositories

import io.chefbook.libs.utils.result.EmptyResult
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

  suspend fun signUp(email: String, password: String): Result<String?>

  suspend fun activateProfile(userId: String, code: String): EmptyResult

  suspend fun signIn(login: String, password: String): Result<Boolean>

  suspend fun signInGoogle(idToken: String): Result<Boolean>

  fun observeProfileDeletionTimestamp(): Flow<String?>

  suspend fun refreshTokens(): EmptyResult
}
