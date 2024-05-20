package io.chefbook.sdk.auth.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface RequestPasswordResetUseCase {

  suspend operator fun invoke(login: String): EmptyResult
}
