package io.chefbook.sdk.auth.api.external.domain.usecases

interface SignInUseCase {

  suspend operator fun invoke(login: String, password: String): Result<Boolean>
}
