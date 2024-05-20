package io.chefbook.sdk.profile.api.external.domain.usecases

interface CheckNicknameAvailabilityUseCase {

  suspend operator fun invoke(nickname: String): Result<Boolean>
}
