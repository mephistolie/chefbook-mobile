package io.chefbook.sdk.profile.impl.domain.usecases

import io.chefbook.sdk.profile.api.external.domain.usecases.CancelProfileDeletionUseCase
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository

internal class CancelProfileDeletionUseCaseImpl(
  private val profileRepository: ProfileRepository,
) : CancelProfileDeletionUseCase {

  override suspend operator fun invoke() =
    profileRepository.cancelProfileDeletion()
}
