package io.chefbook.sdk.settings.api.external.domain.usecases

import io.chefbook.libs.models.language.Language
import kotlinx.coroutines.flow.Flow

interface ObserveCommunityRecipesLanguagesUseCase {
  operator fun invoke(): Flow<List<Language>>
}
