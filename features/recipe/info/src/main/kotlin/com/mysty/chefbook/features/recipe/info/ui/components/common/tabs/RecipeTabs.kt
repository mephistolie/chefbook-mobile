package com.mysty.chefbook.features.recipe.info.ui.components.common.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.ui.common.presentation.RecipeScreenPage

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun RecipeTabs(
  tabsBlockHeight: MutableState<Dp>,
  pagerState: PagerState,
  onTabDoubleClick: () -> Unit,
) {
  val density = LocalDensity.current

  val colors = LocalTheme.colors

  TabRow(
    selectedTabIndex = pagerState.currentPage,
    backgroundColor = colors.backgroundPrimary,
    divider = {
      Divider(
        color = colors.backgroundSecondary,
        modifier = Modifier
          .fillMaxWidth()
          .height(1.dp)
      )
    },
    indicator = { tabPositions ->
      TabRowDefaults.Indicator(
        color = colors.tintPrimary,
        height = 2.dp,
        modifier = Modifier
          .tabIndicatorOffset(tabPositions[pagerState.currentPage])
          .clip(RoundedCornerShape(1.dp)),
      )
    },
    modifier = Modifier
      .background(colors.backgroundPrimary)
      .padding(horizontal = 12.dp)
      .onSizeChanged { size ->
        tabsBlockHeight.value = (size.height / density.density).dp
      },
  ) {
    RecipeScreenPage.values().forEachIndexed { index, tab ->
      RecipeTab(
        tab = tab,
        index = index,
        pagerState = pagerState,
        onTabDoubleClick = onTabDoubleClick,
      )
    }
  }
}
