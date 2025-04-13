package io.chefbook.sdk.auth.impl.data.repositories

import io.chefbook.sdk.auth.impl.data.sources.remote.PasswordSource

internal class PasswordRepositoryImpl(
  private val remoteSource: PasswordSource,
) : PasswordRepository {

  override suspend fun requestPasswordReset(login: String) =
    remoteSource.requestPasswordReset(login)

  override suspend fun resetPassword(userId: String, code: String, newPassword: String) =
    remoteSource.resetPassword(userId, code, newPassword)

  override suspend fun changePassword(oldPassword: String, newPassword: String) =
    remoteSource.changePassword(oldPassword, newPassword)
}
