package io.chefbook.features.community.recipes.ui.viewmodel

import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenEffect
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenIntent
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenState
import io.chefbook.features.community.recipes.ui.mvi.DashboardState
import io.chefbook.libs.coroutines.collectIn
import io.chefbook.libs.logger.Logger
import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import io.chefbook.sdk.tag.api.external.domain.entities.TagGroup
import io.chefbook.sdk.tag.api.external.domain.usecases.ObserveTagsUseCase
import kotlinx.coroutines.CoroutineScope

internal class CommunityRecipesScreenDashboardDelegate(
  private val getState: () -> DashboardState,
  private val updateState: suspend ((DashboardState) -> DashboardState) -> Unit,
  private val changeMode: suspend (CommunityRecipesScreenState.Mode) -> Unit,
  private val reduceIntentParent: suspend (CommunityRecipesScreenIntent) -> Unit,
  private val emitEffect: suspend (CommunityRecipesScreenEffect) -> Unit,
  private val resetResults: suspend () -> Unit,

  private val scope: CoroutineScope,
  private val observeTagsUseCase: ObserveTagsUseCase,
) {

  init {
    observeTags()
  }

  private fun observeTags() {
    observeTagsUseCase().collectIn(scope) { tags ->
      updateState {
        it.copy(tags = tags.orEmpty()
          .asSequence()
          .filter { tag -> tag.group?.id == TagGroup.MENU }
          .filter { tag -> tag.id in dashboardTags }
          .sortedBy { tag -> tag.id }
          .toList()
        )
      }
    }
  }

  suspend fun reduceIntent(intent: CommunityRecipesScreenIntent.Content) {
    when (intent) {
      is CommunityRecipesScreenIntent.Content.TabClicked -> openTab(intent.tab)

      is CommunityRecipesScreenIntent.Content.SearchButtonClicked ->
        emitEffect(CommunityRecipesScreenEffect.FilterOpened(focusSearch = true))

      is CommunityRecipesScreenIntent.Content.FilterButtonClicked ->
        emitEffect(CommunityRecipesScreenEffect.FilterOpened())

      is CommunityRecipesScreenIntent.Content.TagClicked -> {
        reduceIntentParent(CommunityRecipesScreenIntent.Filter.TagSelected(intent.tagId))
        changeMode(CommunityRecipesScreenState.Mode.FILTER)
        resetResults()
      }

      is CommunityRecipesScreenIntent.Content.CreateRecipeClicked ->
        emitEffect(CommunityRecipesScreenEffect.RecipeInputScreenOpened)
    }
  }

  private suspend fun openTab(tab: DashboardState.Tab) {
    if (getState().tab == tab) return

    updateState { it.copy(tab = tab) }
    resetResults()
  }

  companion object {
    private val dashboardTags = setOf(
      Tag.PROPER_NUTRITION,
      Tag.VEGETARIAN_FOOD,
      Tag.WEIGHT_GAIN,
      Tag.SPICY_FOOD,
    )
  }
}