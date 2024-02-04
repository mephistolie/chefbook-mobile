package io.chefbook.sdk.profile.impl.domain.usecases

import io.chefbook.sdk.profile.api.external.domain.usecases.CheckNicknameAvailabilityUseCase
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository

internal class CheckNicknameAvailabilityUseCaseImpl(
  private val repository: ProfileRepository,
) : CheckNicknameAvailabilityUseCase {

  override suspend operator fun invoke(nickname: String) =
    repository.checkNicknameAvailability(nickname)
}
