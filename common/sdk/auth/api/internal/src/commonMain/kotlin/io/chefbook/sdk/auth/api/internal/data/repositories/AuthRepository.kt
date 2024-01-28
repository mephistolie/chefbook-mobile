package io.chefbook.sdk.auth.api.internal.data.repositories

import io.chefbook.libs.utils.result.EmptyResult

interface AuthRepository {
  suspend fun signUp(email: String, password: String): EmptyResult
  suspend fun signIn(login: String, password: String): EmptyResult
}
