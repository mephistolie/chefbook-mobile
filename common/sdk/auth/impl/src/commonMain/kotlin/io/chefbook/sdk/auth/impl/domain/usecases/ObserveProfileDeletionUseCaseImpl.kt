package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.ObserveProfileDeletionUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.SessionRepository

internal class ObserveProfileDeletionUseCaseImpl(
  private val repository: SessionRepository,
) : ObserveProfileDeletionUseCase {

  override operator fun invoke() =
    repository.observeProfileDeletionTimestamp()
}
