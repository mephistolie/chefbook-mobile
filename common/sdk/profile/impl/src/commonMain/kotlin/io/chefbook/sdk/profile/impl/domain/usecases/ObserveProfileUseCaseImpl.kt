package io.chefbook.sdk.profile.impl.domain.usecases

import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository

internal class ObserveProfileUseCaseImpl(
  private val profileRepository: ProfileRepository,
) : ObserveProfileUseCase {

  override operator fun invoke() = profileRepository.observeProfile()
}
