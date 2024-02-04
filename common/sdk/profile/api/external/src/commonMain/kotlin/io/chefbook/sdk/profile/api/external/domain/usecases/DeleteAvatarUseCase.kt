package io.chefbook.sdk.profile.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface DeleteAvatarUseCase {
  suspend operator fun invoke(): EmptyResult
}
