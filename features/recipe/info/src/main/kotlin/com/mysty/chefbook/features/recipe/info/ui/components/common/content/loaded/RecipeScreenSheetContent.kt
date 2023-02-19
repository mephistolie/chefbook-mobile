package com.mysty.chefbook.features.recipe.info.ui.components.common.content.loaded

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.features.recipe.info.ui.components.common.tabs.RecipeTabsHeader
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenIntent
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import kotlinx.coroutines.launch

@OptIn(
  ExperimentalMaterialApi::class, ExperimentalPagerApi::class,
  ExperimentalFoundationApi::class
)
@Composable
internal fun ColumnScope.RecipeScreenSheetContent(
  state: RecipeScreenState.Success,
  onIntent: (RecipeScreenIntent) -> Unit,
  sheetState: BottomSheetState,
  pagerState: PagerState,
  screenHeight: Dp = LocalConfiguration.current.screenHeightDp.dp
) {
  val colors = LocalTheme.colors

  val scope = rememberCoroutineScope()

  val tabsBlockHeight = remember { mutableStateOf(0.dp) }

  Column(
    modifier = Modifier
      .background(colors.backgroundPrimary)
      .fillMaxWidth()
      .wrapContentHeight(),
  ) {
    RecipeTabsHeader(
      tabsBlockHeight = tabsBlockHeight,
      pagerState = pagerState,
      onTabDoubleClick = {
        scope.launch { sheetState.expand() }
      }
    )
    RecipeScreenPager(
      state = state,
      onServingsChanged = { offset ->
        onIntent(RecipeScreenIntent.ChangeServings(offset))
      },
      onIngredientClick = { ingredientId ->
        onIntent(RecipeScreenIntent.ChangeIngredientSelectedStatus(ingredientId))
      },
      onStepPictureClicked = { picture ->
        onIntent(RecipeScreenIntent.OpenPicturesViewer(selectedPicture = picture))
      },
      pagerState = pagerState,
      pageHeight = screenHeight - tabsBlockHeight.value,
    )
  }
}
