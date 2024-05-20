package io.chefbook.sdk.profile.impl.domain.usecases

import io.chefbook.sdk.profile.api.external.domain.usecases.SetAvatarUseCase
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository

internal class SetAvatarUseCaseImpl(
  private val profileRepository: ProfileRepository,
) : SetAvatarUseCase {

  override suspend operator fun invoke(path: String) =
    profileRepository.uploadAvatar(path)
}
