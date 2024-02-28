package io.chefbook.features.community.recipes.ui.screens.tags

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.chefbook.design.components.bottomsheet.BottomSheetBox
import io.chefbook.design.components.bottomsheet.PullBarType
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenIntent
import io.chefbook.features.community.recipes.ui.mvi.FilterState
import io.chefbook.features.community.recipes.ui.screens.tags.components.elements.TagGroup

@Composable
internal fun CommunityRecipesTagGroupScreenContent(
  group: FilterState.TagGroup,
  selectedTags: List<String>,
  onIntent: (CommunityRecipesScreenIntent) -> Unit,
) {

  BottomSheetBox(
    pullBarType = PullBarType.EXTERNAl,
  ) {
    TagGroup(
      group = group,
      selectedTags = selectedTags,
      onTagSelected = { tagId ->
        onIntent(CommunityRecipesScreenIntent.Filter.TagSelected(tagId))
      },
      onTagUnselected = { tagId ->
        onIntent(CommunityRecipesScreenIntent.Filter.TagUnselected(tagId))
      },
      modifier = Modifier.navigationBarsPadding(),
    )
  }
}
