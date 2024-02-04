package io.chefbook.sdk.auth.api.internal.data.repositories

import io.chefbook.libs.utils.result.EmptyResult

interface PasswordRepository {

  suspend fun requestPasswordReset(login: String): EmptyResult

  suspend fun resetPassword(userId: String, code: String, newPassword: String): EmptyResult

  suspend fun changePassword(oldPassword: String, newPassword: String): EmptyResult
}
