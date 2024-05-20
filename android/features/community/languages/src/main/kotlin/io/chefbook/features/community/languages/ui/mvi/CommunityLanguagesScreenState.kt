package io.chefbook.features.community.languages.ui.mvi

import io.chefbook.libs.models.language.Language
import io.chefbook.libs.mvi.MviState

internal data class CommunityLanguagesScreenState(
  val languages: List<Language> = emptyList(),
) : MviState
