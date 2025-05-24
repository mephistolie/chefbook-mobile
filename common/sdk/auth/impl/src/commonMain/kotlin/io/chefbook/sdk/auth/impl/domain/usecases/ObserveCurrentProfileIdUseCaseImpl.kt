package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.ObserveCurrentProfileIdUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.SessionsRepository
import kotlinx.coroutines.flow.Flow

internal class ObserveCurrentProfileIdUseCaseImpl(
  private val repository: SessionsRepository,
) : ObserveCurrentProfileIdUseCase {

  override operator fun invoke(): Flow<String?> =
    repository.observeCurrentProfileId()
}
