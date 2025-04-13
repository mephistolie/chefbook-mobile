package io.chefbook.sdk.auth.impl.domain.usecases

import io.chefbook.sdk.auth.api.external.domain.usecases.ChooseLocalModeUseCase
import io.chefbook.sdk.auth.api.internal.data.repositories.AuthRepository

internal class ChooseLocalModeUseCaseImpl(
  private val authRepository: AuthRepository
) : ChooseLocalModeUseCase {

  override suspend operator fun invoke() = authRepository.signInLocally()
}
