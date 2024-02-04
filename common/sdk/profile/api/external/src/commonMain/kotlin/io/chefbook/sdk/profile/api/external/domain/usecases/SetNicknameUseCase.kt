package io.chefbook.sdk.profile.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface SetNicknameUseCase {

  suspend operator fun invoke(nickname: String): EmptyResult
}
