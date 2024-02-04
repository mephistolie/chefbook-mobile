package io.chefbook.sdk.profile.impl.domain.usecases

import io.chefbook.sdk.profile.api.external.domain.usecases.SetNicknameUseCase
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository

internal class SetNicknameUseCaseImpl(
  private val repository: ProfileRepository,
) : SetNicknameUseCase {

  override suspend operator fun invoke(nickname: String) =
    repository.setNickname(nickname)
}
