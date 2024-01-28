package io.chefbook.features.settings.ui.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.settings.api.external.domain.entities.Settings

internal data class SettingsScreenState(
  val settings: Settings = Settings()
) : MviState
