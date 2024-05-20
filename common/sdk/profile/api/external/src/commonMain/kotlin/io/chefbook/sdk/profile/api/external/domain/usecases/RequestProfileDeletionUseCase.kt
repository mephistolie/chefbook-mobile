package io.chefbook.sdk.profile.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface RequestProfileDeletionUseCase {

  suspend operator fun invoke(password: String, deleteSharedData: Boolean): EmptyResult
}
