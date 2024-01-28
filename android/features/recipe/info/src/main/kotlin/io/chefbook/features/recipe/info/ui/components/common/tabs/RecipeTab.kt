package io.chefbook.features.recipe.info.ui.components.common.tabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.ui.common.presentation.RecipeScreenPage
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun RecipeTab(
  tab: RecipeScreenPage,
  index: Int,
  pagerState: PagerState,
  onTabDoubleClick: () -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val coroutine = rememberCoroutineScope()

  Tab(
    selected = index == pagerState.currentPage,
    selectedContentColor = colors.backgroundPrimary,
    unselectedContentColor = colors.backgroundPrimary,
    onClick = {
      coroutine.launch {
        if (index != pagerState.currentPage) {
          pagerState.animateScrollToPage(index)
        } else {
          onTabDoubleClick()
        }
      }
    },
  ) {
    Text(
      text = stringResource(id = tab.titleId),
      style = typography.headline2,
      color = if (index == pagerState.currentPage) {
        colors.foregroundPrimary
      } else {
        colors.foregroundSecondary
      },
      maxLines = 1,
      modifier = Modifier.padding(vertical = 16.dp)
    )
  }
}
