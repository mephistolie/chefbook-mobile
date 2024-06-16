package io.chefbook.features.recipe.info.ui.components.common.content.loaded

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.padding
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.SmoothCornerShape28Top
import io.chefbook.features.recipe.info.ui.components.common.tabs.RecipeTabsHeader
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenIntent
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import kotlinx.coroutines.launch

@OptIn(
  ExperimentalMaterialApi::class,
  ExperimentalFoundationApi::class
)
@Composable
internal fun RecipeScreenSheetContent(
  state: RecipeScreenState.Success,
  onIntent: (RecipeScreenIntent) -> Unit,
  sheetState: BottomSheetState,
  pagerState: PagerState,
) {
  val screenHeightWithPaddings = LocalConfiguration.current.screenHeightDp.dp
  val navigationBarsPadding =
    WindowInsets.Companion.navigationBars.asPaddingValues().calculateBottomPadding()
  val bottomSheetHeight = screenHeightWithPaddings + navigationBarsPadding

  val colors = LocalTheme.colors

  val scope = rememberCoroutineScope()

  val tabsBlockHeight = remember { mutableStateOf(0.dp) }

  Column(
    modifier = Modifier
      .padding(top = 8.dp)
      .clippedBackground(colors.backgroundPrimary, SmoothCornerShape28Top)
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
      pageHeight = bottomSheetHeight - 8.dp,
    )
  }
}
