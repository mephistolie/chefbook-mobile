package io.chefbook.sdk.auth.api.external.domain.usecases

interface SignUpUseCase {

  suspend operator fun invoke(email: String, password: String): Result<String?>
}
