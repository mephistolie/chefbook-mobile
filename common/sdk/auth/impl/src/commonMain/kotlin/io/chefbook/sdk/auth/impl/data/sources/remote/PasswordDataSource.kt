package io.chefbook.sdk.auth.impl.data.sources.remote

import io.chefbook.libs.utils.result.EmptyResult

internal interface PasswordDataSource {

  suspend fun requestPasswordReset(login: String): EmptyResult

  suspend fun resetPassword(userId: String, code: String, newPassword: String): EmptyResult

  suspend fun changePassword(oldPassword: String, newPassword: String): EmptyResult
}
