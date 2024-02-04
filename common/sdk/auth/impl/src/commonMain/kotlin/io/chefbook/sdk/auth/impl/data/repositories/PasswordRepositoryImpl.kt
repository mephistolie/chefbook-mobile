package io.chefbook.sdk.auth.impl.data.repositories

import io.chefbook.sdk.auth.api.internal.data.repositories.PasswordRepository
import io.chefbook.sdk.auth.impl.data.sources.remote.PasswordDataSource

internal class PasswordRepositoryImpl(
  private val remoteSource: PasswordDataSource,
) : PasswordRepository {

  override suspend fun requestPasswordReset(login: String) =
    remoteSource.requestPasswordReset(login)

  override suspend fun resetPassword(userId: String, code: String, newPassword: String) =
    remoteSource.resetPassword(userId, code, newPassword)

  override suspend fun changePassword(oldPassword: String, newPassword: String) =
    remoteSource.changePassword(oldPassword, newPassword)
}
