package io.chefbook.sdk.settings.impl.domain.usecases

import io.chefbook.libs.models.language.Language
import io.chefbook.sdk.settings.api.external.domain.usecases.SetDefaultRecipeLanguageUseCase
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository

internal class SetDefaultRecipeLanguageUseCaseImpl(
  private val settingsRepository: SettingsRepository,
) : SetDefaultRecipeLanguageUseCase {
  override suspend operator fun invoke(language: Language) =
    settingsRepository.setDefaultRecipeLanguage(language)
}