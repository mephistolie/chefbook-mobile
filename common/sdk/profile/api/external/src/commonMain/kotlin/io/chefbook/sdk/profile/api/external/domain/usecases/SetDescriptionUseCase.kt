package io.chefbook.sdk.profile.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface SetDescriptionUseCase {
  suspend operator fun invoke(description: String?): EmptyResult
}
