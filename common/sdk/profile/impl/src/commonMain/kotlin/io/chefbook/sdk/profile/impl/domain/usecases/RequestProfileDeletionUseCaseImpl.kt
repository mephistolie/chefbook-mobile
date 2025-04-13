package io.chefbook.sdk.profile.impl.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.auth.api.internal.data.repositories.SessionsRepository
import io.chefbook.sdk.profile.api.external.domain.usecases.RequestProfileDeletionUseCase
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository

internal class RequestProfileDeletionUseCaseImpl(
  private val profile: ProfileRepository,
  private val sessions: SessionsRepository,
) : RequestProfileDeletionUseCase {

  override suspend operator fun invoke(
    password: String,
    deleteSharedData: Boolean
  ): EmptyResult {
    profile.requestProfileDeletion(password, deleteSharedData)
      .onFailure { e -> return Result.failure(e) }

    return sessions.refreshTokens()
  }
}
