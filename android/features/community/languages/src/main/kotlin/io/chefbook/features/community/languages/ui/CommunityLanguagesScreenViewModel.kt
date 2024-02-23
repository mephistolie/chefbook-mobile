package io.chefbook.features.community.languages.ui

import io.chefbook.features.community.languages.ui.mvi.CommunityLanguagesScreenEffect
import io.chefbook.features.community.languages.ui.mvi.CommunityLanguagesScreenIntent
import io.chefbook.features.community.languages.ui.mvi.CommunityLanguagesScreenState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.sdk.settings.api.external.domain.usecases.ObserveCommunityRecipesLanguagesUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.SetCommunityRecipesLanguagesUseCase
import kotlinx.coroutines.flow.MutableStateFlow

internal class CommunityLanguagesScreenViewModel(
  private val observeCommunityRecipesLanguagesUseCase: ObserveCommunityRecipesLanguagesUseCase,
  private val setCommunityRecipesLanguagesUseCase: SetCommunityRecipesLanguagesUseCase,
) : BaseMviViewModel<CommunityLanguagesScreenState, CommunityLanguagesScreenIntent, CommunityLanguagesScreenEffect>() {

  override val _state = MutableStateFlow(CommunityLanguagesScreenState())

  init {
    observeLanguages()
  }

  private fun observeLanguages() {
    observeCommunityRecipesLanguagesUseCase().collectState { state, languages ->
      state.copy(languages = languages)
    }
  }

  override suspend fun reduceIntent(intent: CommunityLanguagesScreenIntent) {
    when (intent) {
      is CommunityLanguagesScreenIntent.LanguageSelected -> {
        val languages = state.value.languages.plusElement(intent.language).distinct()
        setCommunityRecipesLanguagesUseCase(languages)
      }
      is CommunityLanguagesScreenIntent.LanguageUnselected -> {
        val languages = state.value.languages.minusElement(intent.language).distinct()
        setCommunityRecipesLanguagesUseCase(languages)
      }
    }
  }
}
