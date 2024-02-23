package io.chefbook.features.community.recipes.ui.screens.filter.components.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.padding
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape28
import io.chefbook.features.community.recipes.R
import io.chefbook.features.community.recipes.ui.screens.filter.components.elements.RadioElement
import io.chefbook.features.community.recipes.ui.mvi.FilterState
import io.chefbook.core.android.R as coreR

internal fun LazyListScope.sortingBlock(
  state: FilterState.Sorting,
  onSortingSelected: (FilterState.Sorting) -> Unit,
  modifier: Modifier = Modifier,
) {
  item {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Column(
      modifier = modifier
        .padding(bottom = 8.dp)
        .background(colors.backgroundPrimary, RoundedCornerShape28)
        .padding(horizontal = 16.dp, top = 12.dp, bottom = 4.dp)
    ) {
      Text(
        text = stringResource(coreR.string.common_general_sorting),
        style = typography.h2,
        color = colors.foregroundPrimary,
        modifier = Modifier
          .padding(top = 4.dp, bottom = 8.dp)
          .wrapContentHeight()
      )
      FilterState.Sorting.entries.forEach { sorting ->
        RadioElement(
          text = when (sorting) {
            FilterState.Sorting.VOTES -> stringResource(coreR.string.common_general_popular)
            FilterState.Sorting.RATING -> stringResource(coreR.string.common_general_top)
            FilterState.Sorting.CREATION_TIMESTAMP -> stringResource(coreR.string.common_general_new)
            FilterState.Sorting.TIME -> stringResource(R.string.common_community_recipes_filter_screen_by_cooking_time)
            FilterState.Sorting.CALORIES -> stringResource(R.string.common_community_recipes_filter_screen_by_calories)
          },
          isSelected = sorting == state,
          onSelected = { onSortingSelected(sorting) },
        )
      }
    }
  }
}
