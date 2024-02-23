package io.chefbook.features.community.languages.ui.mvi

import io.chefbook.libs.models.language.Language
import io.chefbook.libs.mvi.MviIntent

internal sealed interface CommunityLanguagesScreenIntent : MviIntent {
  data class LanguageSelected(val language: Language) : CommunityLanguagesScreenIntent
  data class LanguageUnselected(val language: Language) : CommunityLanguagesScreenIntent
}
