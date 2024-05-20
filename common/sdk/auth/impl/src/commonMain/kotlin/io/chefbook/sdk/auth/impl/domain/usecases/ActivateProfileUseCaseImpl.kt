package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.ActivateProfileUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository

internal class ActivateProfileUseCaseImpl(
  private val authRepository: AuthRepository,
) : ActivateProfileUseCase {

  override suspend operator fun invoke(userId: String, code: String) =
    authRepository.activateProfile(userId, code)
}
