package io.chefbook.features.recipe.info.ui.components.common.tabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.bottomsheet.BottomSheetSlider

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun RecipeTabsHeader(
  tabsBlockHeight: MutableState<Dp>,
  pagerState: PagerState,
  onTabDoubleClick: () -> Unit,
) {
  val colors = LocalTheme.colors

  Column(
    modifier = Modifier.background(colors.backgroundPrimary),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    BottomSheetSlider()
    RecipeTabs(
      tabsBlockHeight = tabsBlockHeight,
      pagerState = pagerState,
      onTabDoubleClick = onTabDoubleClick,
    )
  }
}
