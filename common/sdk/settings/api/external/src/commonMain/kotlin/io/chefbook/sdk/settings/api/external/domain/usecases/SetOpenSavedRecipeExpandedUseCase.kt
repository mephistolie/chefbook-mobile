package io.chefbook.sdk.settings.api.external.domain.usecases

interface SetOpenSavedRecipeExpandedUseCase {
  suspend operator fun invoke(expand: Boolean)
}
