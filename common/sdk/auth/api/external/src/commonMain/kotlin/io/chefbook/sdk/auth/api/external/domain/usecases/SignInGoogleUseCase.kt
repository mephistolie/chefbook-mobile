package io.chefbook.sdk.auth.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface SignInGoogleUseCase {
  suspend operator fun invoke(idToken: String): EmptyResult
}
