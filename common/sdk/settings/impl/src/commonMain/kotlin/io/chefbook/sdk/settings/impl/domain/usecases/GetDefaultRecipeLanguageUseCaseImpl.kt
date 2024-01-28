package io.chefbook.sdk.settings.impl.domain.usecases

import io.chefbook.sdk.settings.api.external.domain.usecases.GetDefaultRecipeLanguageUseCase
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository

internal class GetDefaultRecipeLanguageUseCaseImpl(
  private val settingsRepository: SettingsRepository,
) : GetDefaultRecipeLanguageUseCase {

  override suspend operator fun invoke() = settingsRepository.getDefaultRecipeLanguage()
}