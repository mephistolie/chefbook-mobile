package com.mysty.chefbook.features.home.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import com.mysty.chefbook.api.settings.domain.entities.Tab
import com.mysty.chefbook.design.components.bottomsheet.TopBarBottomSheet
import com.mysty.chefbook.design.components.bottomsheet.rememberTopBarBottomSheetState
import com.mysty.chefbook.features.home.ui.components.HomeScreenTopBar
import com.mysty.chefbook.features.home.ui.components.animatedBackground
import com.mysty.chefbook.features.home.ui.mvi.HomeScreenIntent
import com.mysty.chefbook.features.home.ui.mvi.HomeScreenState
import com.mysty.chefbook.features.home.ui.navigation.IHomeScreenNavGraph
import com.mysty.chefbook.ui.common.providers.BottomSheetExpandProgressProvider

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenContent(
  homeState: HomeScreenState,
  onIntent: (HomeScreenIntent) -> Unit,
  navGraph: IHomeScreenNavGraph,
) {
  val sheetState = rememberTopBarBottomSheetState()
  var visibleTab by remember { mutableStateOf(homeState.currentTab) }
  var isInit by remember { mutableStateOf(true) }

  BottomSheetExpandProgressProvider(progress = sheetState) {
    TopBarBottomSheet(
      sheetState = sheetState,
      backgroundColor = { progress ->
        animatedBackground(progress = progress, tab = homeState.currentTab)
      },
      sheetShape = RectangleShape,
      sheetContent = {
        when (visibleTab) {
          Tab.RECIPE_BOOK -> navGraph.RecipeBookScreen()
          Tab.SHOPPING_LIST -> navGraph.ShoppingListScreen()
        }
      },
    ) {
      HomeScreenTopBar(
        currentTab = homeState.currentTab,
        profileAvatar = homeState.profileAvatar,
        onIntent = onIntent,
      )
    }
  }

  LaunchedEffect(homeState.currentTab) {
    if (isInit) {
      sheetState.collapse()
      isInit = true
    }
    if (homeState.currentTab != visibleTab) {
      sheetState.reopen(onHiddenCallback = { visibleTab = homeState.currentTab })
    }
  }
}
