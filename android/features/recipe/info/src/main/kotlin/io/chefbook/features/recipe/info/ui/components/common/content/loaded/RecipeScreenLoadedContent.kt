package io.chefbook.features.recipe.info.ui.components.common.content.loaded

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import io.chefbook.core.compose.providers.theme.LocalTheme
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
) {
  val configuration = LocalConfiguration.current

  val colors = LocalTheme.colors

  val pagerState = rememberPagerState { pages.size }

  val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

  val paddings = WindowInsets.Companion.systemBars.asPaddingValues()

  val infoCardHeight = remember { mutableStateOf(0.dp) }

  val expandTransition =
    updateTransition(scaffoldState.bottomSheetState.targetValue, label = "isExpanded")
  val background = expandTransition.animateColor(label = "background") { value ->
    when {
      colors.isDark -> if (value == BottomSheetValue.Expanded) colors.backgroundSecondary else Color.Black
      else -> colors.backgroundSecondary
    }
  }

  BottomSheetScaffold(
    scaffoldState = scaffoldState,
    backgroundColor = background.value,
    content = {
      RecipeScreenSurfaceContent(
        state = state,
        onIntent = onIntent,
        bottomSheetState = scaffoldState.bottomSheetState,
        setCardHeight = { infoCardHeight.value = it },
      )
    },
    sheetBackgroundColor = Color.Transparent,
    sheetPeekHeight = configuration.screenHeightDp.dp - infoCardHeight.value + paddings.calculateBottomPadding(),
    sheetElevation = 0.dp,
    sheetContent = {
      RecipeScreenSheetContent(
        state = state,
        onIntent = onIntent,
        sheetState = sheetState,
        pagerState = pagerState,
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
