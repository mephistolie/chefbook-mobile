package io.chefbook.sdk.profile.impl.domain.usecases

import io.chefbook.sdk.profile.api.external.domain.usecases.SetNameUseCase
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository

internal class SetNameUseCaseImpl(
  private val profileRepository: ProfileRepository,
) : SetNameUseCase {

  override suspend operator fun invoke(firstName: String?, lastName: String?) =
    profileRepository.setName(firstName, lastName)
}
