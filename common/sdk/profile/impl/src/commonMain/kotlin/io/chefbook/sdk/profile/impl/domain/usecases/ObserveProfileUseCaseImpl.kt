package io.chefbook.sdk.profile.impl.domain.usecases

import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository
import kotlinx.coroutines.flow.filterNotNull

internal class ObserveProfileUseCaseImpl(
  private val profileRepository: ProfileRepository,
) : ObserveProfileUseCase {

  override operator fun invoke() = profileRepository.observeProfile()
}
