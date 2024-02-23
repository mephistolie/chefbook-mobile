package io.chefbook.features.community.recipes.ui.screens.content.components.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.core.android.R as coreR
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.theme.dimens.ButtonSmallHeight
import io.chefbook.features.community.recipes.ui.mvi.DashboardState
import kotlinx.coroutines.launch

@Composable
internal fun Tabs(
  selectedTab: DashboardState.Tab,
  onTabClick: (DashboardState.Tab) -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val startPadding = with(LocalDensity.current) { 12.dp.toPx() }.toInt()

  val scope = rememberCoroutineScope()

  val scrollState = rememberLazyListState()

  LazyRow(
    state = scrollState,
    modifier = modifier
      .fillMaxWidth()
      .padding(top = 12.dp, bottom = 12.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    DashboardState.Tab.entries.forEachIndexed { index, tab ->
      item {
        DynamicButton(
          text = when (tab) {
            DashboardState.Tab.NEW -> stringResource(coreR.string.common_general_new)
            DashboardState.Tab.VOTES -> stringResource(coreR.string.common_general_popular)
            DashboardState.Tab.TOP -> stringResource(coreR.string.common_general_top)
            DashboardState.Tab.FAST -> stringResource(coreR.string.common_general_fast)
          },
          cornerRadius = 12.dp,
          textStyle = if (tab == selectedTab) typography.h4 else typography.headline1,
          isSelected = tab == selectedTab,
          selectedForeground = colors.foregroundPrimary,
          selectedBackground = colors.backgroundSecondary,
          unselectedForeground = colors.foregroundSecondary,
          unselectedBackground = colors.backgroundPrimary,
          modifier = Modifier
            .padding(start = if (index == 0) 12.dp else 0.dp)
            .height(ButtonSmallHeight),
          onClick = {
            scope.launch {
              scrollState.animateScrollToItem(index, scrollOffset = -startPadding)
            }
            onTabClick(tab)
          },
        )
      }
    }
  }
}
