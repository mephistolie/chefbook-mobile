package io.chefbook.sdk.auth.api.internal.data.repositories

import io.chefbook.libs.utils.result.EmptyResult

interface AuthRepository {

  suspend fun signUp(email: String, password: String): Result<String?>

  suspend fun activateProfile(userId: String, code: String): EmptyResult

  suspend fun signIn(login: String, password: String): EmptyResult

  suspend fun signInGoogle(idToken: String): EmptyResult

  suspend fun requestPasswordReset(login: String): EmptyResult

  suspend fun resetPassword(userId: String, code: String, newPassword: String): EmptyResult

  suspend fun changePassword(oldPassword: String, newPassword: String): EmptyResult
}
