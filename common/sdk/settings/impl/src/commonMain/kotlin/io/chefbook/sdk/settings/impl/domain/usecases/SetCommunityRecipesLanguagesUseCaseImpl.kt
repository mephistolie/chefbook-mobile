package io.chefbook.sdk.settings.impl.domain.usecases

import io.chefbook.libs.models.language.Language
import io.chefbook.sdk.settings.api.external.domain.usecases.SetCommunityRecipesLanguagesUseCase
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository

internal class SetCommunityRecipesLanguagesUseCaseImpl(
  private val settingsRepository: SettingsRepository,
) : SetCommunityRecipesLanguagesUseCase {

  override suspend operator fun invoke(languages: List<Language>) =
    settingsRepository.setCommunityRecipesLanguages(languages)
}
