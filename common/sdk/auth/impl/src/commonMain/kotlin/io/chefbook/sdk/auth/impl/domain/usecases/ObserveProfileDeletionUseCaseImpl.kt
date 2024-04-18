package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.ObserveProfileDeletionUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository

internal class ObserveProfileDeletionUseCaseImpl(
  private val authRepository: AuthRepository,
) : ObserveProfileDeletionUseCase {

  override operator fun invoke() =
    authRepository.observeProfileDeletionTimestamp()
}
