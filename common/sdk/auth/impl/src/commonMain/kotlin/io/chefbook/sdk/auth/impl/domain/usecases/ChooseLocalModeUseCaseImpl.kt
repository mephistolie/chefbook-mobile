package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.ChooseLocalModeUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.CurrentSessionRepository

internal class ChooseLocalModeUseCaseImpl(
  private val sessionRepository: CurrentSessionRepository
) : ChooseLocalModeUseCase {

  override suspend operator fun invoke() =
    sessionRepository.finishSession()
}
