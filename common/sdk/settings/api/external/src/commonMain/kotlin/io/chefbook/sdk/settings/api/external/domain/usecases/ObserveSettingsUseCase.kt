package io.chefbook.sdk.settings.api.external.domain.usecases

import io.chefbook.sdk.settings.api.external.domain.entities.Settings
import kotlinx.coroutines.flow.Flow

interface ObserveSettingsUseCase {
  operator fun invoke(): Flow<Settings>
}
