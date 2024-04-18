package io.chefbook.sdk.profile.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface SetAvatarUseCase {

  suspend operator fun invoke(path: String): EmptyResult
}
