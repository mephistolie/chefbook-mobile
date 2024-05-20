package io.chefbook.sdk.auth.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface RestoreProfileUseCase {

  suspend operator fun invoke(): EmptyResult
}
