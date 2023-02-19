package com.mysty.chefbook.features.recipe.info.ui.components.common.content.loaded

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.theme.shapes.ModalBottomSheetShape
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenIntent
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import com.mysty.chefbook.ui.common.presentation.RecipeScreenPage

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
internal fun BoxScope.RecipeScreenLoadedContent(
  state: RecipeScreenState.Success,
  initPage: RecipeScreenPage,
  onIntent: (RecipeScreenIntent) -> Unit,
  openExpanded: Boolean = false,
  screenHeight: Dp = LocalConfiguration.current.screenHeightDp.dp
) {
  val colors = LocalTheme.colors

  val pagerState = rememberPagerState()

  val contentHeight = remember { mutableStateOf(0.dp) }
  val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
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
    sheetShape = ModalBottomSheetShape,
    sheetPeekHeight = screenHeight - contentHeight.value,
    sheetElevation = 24.dp,
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
    isVisible = RecipeScreenPage.values()[pagerState.currentPage] == RecipeScreenPage.INGREDIENTS &&
                state.selectedIngredients.isNotEmpty(),
    onClick = { onIntent(RecipeScreenIntent.AddSelectedIngredientsToShoppingList) }
  )

  LaunchedEffect(initPage) {
    pagerState.scrollToPage(RecipeScreenPage.values().indexOf(initPage))
    if (openExpanded) sheetState.expand()
  }
}
