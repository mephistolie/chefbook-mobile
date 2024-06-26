package io.chefbook.features.community.recipes.ui.viewmodel

import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenEffect
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenIntent
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenState
import io.chefbook.features.community.recipes.ui.mvi.FilterState
import io.chefbook.libs.coroutines.collectIn
import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import io.chefbook.sdk.tag.api.external.domain.entities.TagGroup
import io.chefbook.sdk.tag.api.external.domain.usecases.ObserveTagsUseCase
import kotlinx.coroutines.CoroutineScope

internal class CommunityRecipesScreenFilterDelegate(
  private val getState: () -> FilterState,
  private val updateState: suspend ((FilterState) -> FilterState) -> Unit,
  private val changeMode: suspend (CommunityRecipesScreenState.Mode) -> Unit,
  private val emitEffect: suspend (CommunityRecipesScreenEffect) -> Unit,
  private val resetResults: suspend () -> Unit,

  private val scope: CoroutineScope,
  private val observeTagsUseCase: ObserveTagsUseCase,
) {

  private var lastAppliedFilter = getState()

  init {
    observeTags()
  }

  private fun observeTags() {
    observeTagsUseCase().collectIn(scope) { tags ->
      updateState { state -> state.copy(tagGroups = tags.orEmpty().byGroups()) }
    }
  }

  suspend fun reduceIntent(intent: CommunityRecipesScreenIntent.Filter) {
    when (intent) {
      is CommunityRecipesScreenIntent.Filter.SetSearchText ->
        updateState { it.copy(search = intent.text.take(MAX_SEARCH_INPUT).ifBlank { null }) }

      is CommunityRecipesScreenIntent.Filter.ChangeHighRatingOnly ->
        updateState { it.copy(highRatingOnly = !it.highRatingOnly) }

      is CommunityRecipesScreenIntent.Filter.SetMinCalories ->
        updateState { it.copy(minCalories = intent.calories) }

      is CommunityRecipesScreenIntent.Filter.SetMaxCalories ->
        updateState { it.copy(maxCalories = intent.calories) }

      is CommunityRecipesScreenIntent.Filter.SetSorting ->
        updateState { it.copy(sorting = intent.sorting) }

      is CommunityRecipesScreenIntent.Filter.TagSelected ->
        updateState {
          it.copy(selectedTags = it.selectedTags.plusElement(intent.tagId).distinct())
        }

      is CommunityRecipesScreenIntent.Filter.TagUnselected ->
        updateState { it.copy(selectedTags = it.selectedTags.minusElement(intent.tagId)) }

      is CommunityRecipesScreenIntent.Filter.ExpandTagGroupClicked ->
        emitEffect(CommunityRecipesScreenEffect.TagGroupOpened(intent.groupId))

      is CommunityRecipesScreenIntent.Filter.TagGroupClosed ->
        emitEffect(CommunityRecipesScreenEffect.TagGroupClosed)

      is CommunityRecipesScreenIntent.Filter.ResetFilterClicked ->
        updateState { FilterState(tagGroups = it.tagGroups) }

      is CommunityRecipesScreenIntent.Filter.ConfirmFilterClicked -> {
        val state = getState()

        val mode = if (state.isDefault) {
          CommunityRecipesScreenState.Mode.DASHBOARD
        } else {
          CommunityRecipesScreenState.Mode.FILTER
        }
        changeMode(mode)

        if (lastAppliedFilter != state) {
          lastAppliedFilter = state
          resetResults()
        }

        emitEffect(CommunityRecipesScreenEffect.FilterClosed)
      }

      is CommunityRecipesScreenIntent.Filter.CloseFilter -> {
        updateState { lastAppliedFilter.copy(tagGroups = it.tagGroups) }
        emitEffect(CommunityRecipesScreenEffect.FilterClosed)
      }

      is CommunityRecipesScreenIntent.Filter.FilterClosed ->
        updateState { lastAppliedFilter.copy(tagGroups = it.tagGroups) }
    }
  }

  private fun List<Tag>.byGroups() = map { it.group }
    .distinct()
    .sortedWith { first, second ->
      return@sortedWith (tagGroupsMap[first?.id] ?: 0) - (tagGroupsMap[second?.id] ?: 0)
    }
    .map { group ->
      FilterState.TagGroup(
        id = group?.id,
        name = group?.name,
        tags = this
          .filter { it.group == group }
          .sortedBy { it.name },
      )
    }

  companion object {

    private const val MAX_SEARCH_INPUT = 64

    private val tagGroupsMap = mapOf(
      TagGroup.MENU to 0,
      TagGroup.FOOD_TYPE to 1,
      TagGroup.MEAL_TIME to 2,
      TagGroup.CUISINE to 3,
    )
  }
}