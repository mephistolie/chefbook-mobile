package io.chefbook.sdk.profile.impl.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository
import io.chefbook.sdk.profile.api.external.domain.usecases.RequestProfileDeletionUseCase
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository

internal class RequestProfileDeletionUseCaseImpl(
  private val profileRepository: ProfileRepository,
  private val authRepository: AuthRepository,
) : RequestProfileDeletionUseCase {

  override suspend operator fun invoke(password: String, deleteSharedData: Boolean): EmptyResult {
    profileRepository.requestProfileDeletion(password, deleteSharedData).onFailure { e -> return Result.failure(e) }
    authRepository.refreshTokens()

    return successResult
  }
}
