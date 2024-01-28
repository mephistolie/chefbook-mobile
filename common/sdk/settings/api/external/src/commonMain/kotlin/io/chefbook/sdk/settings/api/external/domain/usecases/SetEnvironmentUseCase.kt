package io.chefbook.sdk.settings.api.external.domain.usecases

import io.chefbook.sdk.settings.api.external.domain.entities.Environment

interface SetEnvironmentUseCase {
  suspend operator fun invoke(environment: Environment)
}
