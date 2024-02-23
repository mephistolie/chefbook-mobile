package io.chefbook.features.community.recipes.ui.screens.content.components.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.dimens.DefaultIconSize
import io.chefbook.design.theme.shapes.BottomSheetShape
import io.chefbook.features.community.recipes.R
import io.chefbook.features.community.recipes.ui.mvi.FilterState
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

internal val filterBlockHeight = 64.dp

@Composable
internal fun FilterBlock(
  filter: FilterState,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val title = when {
    filter.search != null -> filter.search
    filter.selectedTags.isNotEmpty() -> {
      when (filter.selectedTags.size) {
        1 -> filter.tagGroups
          .flatMap { it.tags }
          .firstOrNull { it.id == filter.selectedTags.first() }
          ?.name.orEmpty()
        else -> "${stringResource(coreR.string.common_general_tags)}: ${filter.selectedTags.size}"
      }
    }

    else -> when (filter.sorting) {
      FilterState.Sorting.VOTES -> stringResource(coreR.string.common_general_popular)
      FilterState.Sorting.RATING -> stringResource(coreR.string.common_general_top)
      FilterState.Sorting.CREATION_TIMESTAMP -> stringResource(coreR.string.common_general_new)
      FilterState.Sorting.TIME -> stringResource(R.string.common_community_recipes_filter_screen_by_cooking_time)
      FilterState.Sorting.CALORIES -> stringResource(R.string.common_community_recipes_filter_screen_by_calories)
    }
  }

  Row(
    modifier = modifier
      .clippedBackground(colors.backgroundSecondary, BottomSheetShape)
      .padding(top = 1.dp)
      .clippedBackground(colors.backgroundPrimary, BottomSheetShape)
      .navigationBarsPadding()
      .fillMaxWidth()
      .height(filterBlockHeight)
      .padding(12.dp)
      .simpleClickable(debounceInterval = 1000L, onClick = onClick),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Icon(
      imageVector = ImageVector.vectorResource(designR.drawable.ic_slider),
      tint = colors.foregroundPrimary,
      contentDescription = null,
      modifier = Modifier.size(DefaultIconSize),
    )
    Column(
      modifier = Modifier
        .weight(1F)
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = title,
        textAlign = TextAlign.Center,
        style = typography.h4,
        color = colors.foregroundPrimary,
      )
    }
    Icon(
      imageVector = ImageVector.vectorResource(designR.drawable.ic_arrow_right),
      tint = colors.foregroundPrimary,
      contentDescription = null,
      modifier = Modifier
        .size(DefaultIconSize)
        .padding(4.dp),
    )
  }
}
