package io.chefbook.features.community.recipes.ui.screens.filter

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.bottomsheet.BottomSheetBox
import io.chefbook.features.community.recipes.ui.screens.filter.components.blocks.ButtonsBlock
import io.chefbook.features.community.recipes.ui.screens.filter.components.blocks.buttonsBlockHeight
import io.chefbook.features.community.recipes.ui.screens.filter.components.blocks.caloriesBlock
import io.chefbook.features.community.recipes.ui.screens.filter.components.blocks.searchBlock
import io.chefbook.features.community.recipes.ui.screens.filter.components.blocks.sortingBlock
import io.chefbook.features.community.recipes.ui.screens.filter.components.blocks.specificitiesBlock
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenIntent
import io.chefbook.features.community.recipes.ui.mvi.FilterState
import io.chefbook.features.community.recipes.ui.screens.filter.components.blocks.tagGroupsBlocks
import io.chefbook.features.community.recipes.ui.screens.tags.components.elements.TagGroup

private const val FIRST_TAG_GROUP_INDEX = 4

@Composable
internal fun CommunityRecipesFilterScreenContent(
  state: FilterState,
  onIntent: (CommunityRecipesScreenIntent) -> Unit,
  focusSearch: Boolean,
  scrollToTags: Boolean,
) {
  val colors = LocalTheme.colors

  val listState = rememberLazyListState()

  BottomSheetBox(backgroundColor = colors.divider) {
    LazyColumn(
      state = listState,
      modifier = Modifier.fillMaxSize(),
    ) {
      searchBlock(
        search = state.search.orEmpty(),
        onSearchChange = { onIntent(CommunityRecipesScreenIntent.Filter.SetSearchText(it)) },
        focusSearch = focusSearch,
      )
      specificitiesBlock(
        highRatingOnly = state.highRatingOnly,
        onHighRatingOnlyClick = { onIntent(CommunityRecipesScreenIntent.Filter.ChangeHighRatingOnly) }
      )
      caloriesBlock(
        minCalories = state.minCalories,
        maxCalories = state.maxCalories,
        onMinCaloriesChange = { onIntent(CommunityRecipesScreenIntent.Filter.SetMinCalories(it)) },
        onMaxCaloriesChange = { onIntent(CommunityRecipesScreenIntent.Filter.SetMaxCalories(it)) },
      )
      sortingBlock(
        state = state.sorting,
        onSortingSelected = { onIntent(CommunityRecipesScreenIntent.Filter.SetSorting(it)) },
      )
      tagGroupsBlocks(
        groups = state.tagGroups,
        selectedTags = state.selectedTags,
        onTagSelected = { tagId ->
          onIntent(CommunityRecipesScreenIntent.Filter.TagSelected(tagId))
        },
        onTagUnselected = { tagId ->
          onIntent(CommunityRecipesScreenIntent.Filter.TagUnselected(tagId))
        },
        onTagGroupExpandClicked = { groupId ->
          onIntent(CommunityRecipesScreenIntent.Filter.ExpandTagGroupClicked(groupId))
        }
      )
      item {
        Spacer(
          modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
            .height(buttonsBlockHeight)
        )
      }
    }
    ButtonsBlock(
      isResetButtonVisible = !state.isDefault,
      onResetClick = {
        onIntent(CommunityRecipesScreenIntent.Filter.ResetFilterClicked)
      },
      onConfirmClick = {
        onIntent(CommunityRecipesScreenIntent.Filter.ConfirmFilterClicked)
      },
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }

  LaunchedEffect(scrollToTags, state.tagGroups) {
    if (scrollToTags && state.tagGroups.isNotEmpty()) {
      listState.animateScrollToItem(FIRST_TAG_GROUP_INDEX)
    }
  }
}
