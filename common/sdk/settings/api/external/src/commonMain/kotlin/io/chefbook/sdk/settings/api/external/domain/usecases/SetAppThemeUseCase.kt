package io.chefbook.sdk.settings.api.external.domain.usecases

import io.chefbook.sdk.settings.api.external.domain.entities.AppTheme

interface SetAppThemeUseCase {
  suspend operator fun invoke(theme: AppTheme)
}
