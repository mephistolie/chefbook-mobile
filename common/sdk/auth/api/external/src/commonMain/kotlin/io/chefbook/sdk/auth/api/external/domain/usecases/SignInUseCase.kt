package io.chefbook.sdk.auth.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface SignInUseCase {
  suspend operator fun invoke(login: String, password: String): EmptyResult
}
