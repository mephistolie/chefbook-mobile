package io.chefbook.features.root.ui.mvi

import io.chefbook.sdk.settings.api.external.domain.entities.AppTheme

data class RootState(
  val theme: AppTheme = AppTheme.SYSTEM,
)
