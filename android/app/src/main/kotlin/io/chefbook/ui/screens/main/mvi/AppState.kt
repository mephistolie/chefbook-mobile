package io.chefbook.ui.screens.main.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.settings.api.external.domain.entities.AppTheme

data class AppState(
  val profileId: String? = null,
  val theme: AppTheme = AppTheme.SYSTEM,
) : MviState
