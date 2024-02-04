package io.chefbook.sdk.auth.impl.data.sources.remote

import io.chefbook.libs.utils.auth.isEmail
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.PasswordApiService
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto.ChangePasswordRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto.RequestPasswordResetRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.password.dto.ResetPasswordRequest

internal class PasswordDataSourceImpl(
  private val api: PasswordApiService,
) : PasswordDataSource {

  override suspend fun requestPasswordReset(login: String): EmptyResult {
    val isEmail = isEmail(login)
    val email = if (isEmail) login else null
    val nickname = if (isEmail) null else login

    return api.requestPasswordReset(
      RequestPasswordResetRequest(
        email = email,
        nickname = nickname,
      )
    ).asEmpty()
  }

  override suspend fun resetPassword(userId: String, code: String, newPassword: String) =
    api.resetPassword(
      ResetPasswordRequest(
        userId = userId,
        resetCode = code,
        newPassword = newPassword,
      )
    ).asEmpty()

  override suspend fun changePassword(oldPassword: String, newPassword: String) =
    api.changePassword(
      ChangePasswordRequest(
        oldPassword = oldPassword,
        newPassword = newPassword,
      )
    ).asEmpty()
}
