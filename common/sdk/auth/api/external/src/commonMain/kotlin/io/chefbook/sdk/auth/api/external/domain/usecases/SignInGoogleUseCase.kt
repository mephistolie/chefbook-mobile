package io.chefbook.sdk.auth.api.external.domain.usecases

interface SignInGoogleUseCase {

  suspend operator fun invoke(idToken: String): Result<Boolean>
}
