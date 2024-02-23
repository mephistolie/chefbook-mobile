package io.chefbook.sdk.settings.impl.domain.usecases

import io.chefbook.sdk.settings.api.external.domain.usecases.ObserveCommunityRecipesLanguagesUseCase
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository

internal class ObserveCommunityRecipesLanguagesUseCaseImpl(
  private val settingsRepository: SettingsRepository,
) : ObserveCommunityRecipesLanguagesUseCase {

  override operator fun invoke() = settingsRepository.observeCommunityRecipesLanguages()
}
