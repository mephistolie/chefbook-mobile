package io.chefbook.sdk.auth.impl.data.sources.remote

import io.chefbook.libs.utils.result.EmptyResult
import io.ktor.client.plugins.auth.providers.BearerTokens

internal interface AuthDataSource {

  suspend fun signUp(email: String, password: String): Result<String?>

  suspend fun activateProfile(userId: String, code: String): EmptyResult

  suspend fun signIn(login: String, password: String): Result<BearerTokens>

  suspend fun signInGoogle(token: String): Result<BearerTokens>

  suspend fun signOut(refreshToken: String): EmptyResult
}
