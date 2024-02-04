package io.chefbook.sdk.profile.impl.domain.usecases

import io.chefbook.sdk.profile.api.external.domain.usecases.SetDescriptionUseCase
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository

internal class SetDescriptionUseCaseImpl(
  private val profileRepository: ProfileRepository,
) : SetDescriptionUseCase {

  override suspend operator fun  invoke(description: String?) =
    profileRepository.setDescription(description)
}
