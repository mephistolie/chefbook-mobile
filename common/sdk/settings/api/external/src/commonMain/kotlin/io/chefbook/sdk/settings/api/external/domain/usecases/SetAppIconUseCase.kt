package io.chefbook.sdk.settings.api.external.domain.usecases

import io.chefbook.sdk.settings.api.external.domain.entities.AppIcon

interface SetAppIconUseCase {
  suspend operator fun invoke(icon: AppIcon)
}
