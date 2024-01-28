package io.chefbook.sdk.settings.impl.domain.usecases

import io.chefbook.sdk.settings.api.external.domain.usecases.SetOpenSavedRecipeExpandedUseCase
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository

internal class SetOpenSavedRecipeExpandedUseCaseImpl(
  private val settingsRepository: SettingsRepository,
) : SetOpenSavedRecipeExpandedUseCase {
  override suspend operator fun invoke(expand: Boolean) =
    settingsRepository.setOpenSavedRecipeExpanded(expand)
}