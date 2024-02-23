package io.chefbook.sdk.settings.api.external.domain.usecases

import io.chefbook.libs.models.language.Language

interface SetCommunityRecipesLanguagesUseCase {
  suspend operator fun invoke(languages: List<Language>)
}
