package io.chefbook.sdk.profile.impl.domain.usecases

import io.chefbook.sdk.profile.api.external.domain.usecases.DeleteAvatarUseCase
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository

internal class DeleteAvatarUseCaseImpl(
  private val profileRepository: ProfileRepository,
) : DeleteAvatarUseCase {

  override suspend operator fun invoke() =
    profileRepository.deleteAvatar()
}
