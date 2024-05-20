package io.chefbook.features.recipe.info.ui.components.common.content.loaded

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape28Top
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenIntent
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import io.chefbook.ui.common.presentation.RecipeScreenPage

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
internal fun BoxScope.RecipeScreenLoadedContent(
  state: RecipeScreenState.Success,
  initPage: RecipeScreenPage,
  onIntent: (RecipeScreenIntent) -> Unit,
  sheetState: BottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed),
  openExpanded: Boolean = false,
  screenHeight: Dp = LocalConfiguration.current.screenHeightDp.dp
) {
  val colors = LocalTheme.colors

  val pagerState = rememberPagerState { pages.size }

  val contentHeight = remember { mutableStateOf(0.dp) }

  val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

  BottomSheetScaffold(
    scaffoldState = scaffoldState,
    modifier = Modifier.padding(top = 8.dp),
    backgroundColor = colors.backgroundSecondary,
    content = {
      RecipeScreenSurfaceContent(
        state = state,
        onIntent = onIntent,
        contentHeight = contentHeight,
      )
    },
    sheetShape = RoundedCornerShape28Top,
    sheetPeekHeight = screenHeight - contentHeight.value,
    sheetElevation = 0.dp,
    sheetContent = {
      RecipeScreenSheetContent(
        state = state,
        onIntent = onIntent,
        sheetState = sheetState,
        pagerState = pagerState,
        screenHeight = screenHeight,
      )
    }
  )
  AddToShoppingListFab(
    isVisible = pages[pagerState.currentPage] == RecipeScreenPage.INGREDIENTS &&
                state.selectedIngredients.isNotEmpty(),
    onClick = { onIntent(RecipeScreenIntent.AddSelectedIngredientsToShoppingList) }
  )

  LaunchedEffect(initPage) {
    pagerState.scrollToPage(pages.indexOf(initPage))
    if (openExpanded) sheetState.expand()
  }
}
