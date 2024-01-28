package io.chefbook.sdk.auth.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface SignUpUseCase {
  suspend operator fun invoke(email: String, password: String): EmptyResult
}
