package io.chefbook.sdk.settings.impl.domain.usecases

import io.chefbook.sdk.settings.api.external.domain.usecases.GetSettingsUseCase
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository

internal class GetSettingsUseCaseImpl(
  private val settingsRepository: SettingsRepository,
) : GetSettingsUseCase {

  override suspend operator fun invoke() = settingsRepository.getSettings()
}
