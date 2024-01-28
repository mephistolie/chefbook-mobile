package io.chefbook.sdk.settings.api.external.domain.usecases

import io.chefbook.sdk.settings.api.external.domain.entities.Settings

interface GetSettingsUseCase {
  suspend operator fun invoke(): Settings
}
