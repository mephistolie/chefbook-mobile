package io.chefbook.features.community.recipes.ui.viewmodel

import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenState
import io.chefbook.libs.coroutines.collectIn
import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import io.chefbook.sdk.settings.api.external.domain.usecases.ObserveCommunityRecipesLanguagesUseCase
import kotlinx.coroutines.CoroutineScope

internal class CommunityRecipesScreenExternalDelegate(
  private val updateState: suspend ((CommunityRecipesScreenState) -> CommunityRecipesScreenState) -> Unit,
  private val resetResults: suspend () -> Unit,

  private val scope: CoroutineScope,
  private val observeProfileUseCase: ObserveProfileUseCase,
  private val observeLanguagesUseCase: ObserveCommunityRecipesLanguagesUseCase,
) {

  init {
    observeProfile()
    observeLanguages()
  }

  private fun observeProfile() {
    observeProfileUseCase().collectIn(scope) { profile ->
      updateState { it.copy(profileAvatar = profile?.avatar) }
    }
  }

  private fun observeLanguages() {
    observeLanguagesUseCase().collectIn(scope) { languages ->
      updateState { it.copy(languages = languages) }
      resetResults()
    }
  }
}
