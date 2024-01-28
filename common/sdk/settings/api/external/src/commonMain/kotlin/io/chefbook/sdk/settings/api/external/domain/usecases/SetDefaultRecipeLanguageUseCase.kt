package io.chefbook.sdk.settings.api.external.domain.usecases

import io.chefbook.libs.models.language.Language

fun interface SetDefaultRecipeLanguageUseCase {
  suspend operator fun invoke(language: Language)
}
