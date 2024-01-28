package io.chefbook.sdk.settings.api.external.domain.usecases

import io.chefbook.libs.models.language.Language

interface GetDefaultRecipeLanguageUseCase {
  suspend operator fun invoke(): Language
}
