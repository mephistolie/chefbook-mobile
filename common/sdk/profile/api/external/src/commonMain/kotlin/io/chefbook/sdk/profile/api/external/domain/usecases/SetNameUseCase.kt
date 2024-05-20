package io.chefbook.sdk.profile.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface SetNameUseCase {

  suspend operator fun invoke(firstName: String?, lastName: String?): EmptyResult
}
