package io.chefbook.sdk.auth.api.internal.data.repositories

import io.chefbook.libs.utils.result.EmptyResult

interface AuthRepository {

  suspend fun signUp(email: String, password: String): Result<String?>

  suspend fun activateProfile(userId: String, code: String): EmptyResult

  suspend fun signIn(login: String, password: String): Result<Boolean>

  suspend fun signInLocally()

  suspend fun signInGoogle(idToken: String): Result<Boolean>

  suspend fun signOut()
}
