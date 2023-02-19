package com.mysty.chefbook.features.recipe.control.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.mephistolie.compost.modifiers.padding
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.bottomsheet.BottomSheetSlider
import com.mysty.chefbook.features.recipe.control.ui.components.categories.RecipeCategoriesSelectionBlock
import com.mysty.chefbook.features.recipe.control.ui.components.menu.RecipeControlScreenMenu
import com.mysty.chefbook.features.recipe.control.ui.mvi.RecipeControlScreenIntent
import com.mysty.chefbook.features.recipe.control.ui.mvi.RecipeControlScreenState
import com.mysty.chefbook.features.recipe.control.ui.presentation.RecipeControlScreenPage
import com.mysty.chefbook.navigation.navigators.IBaseNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun RecipeControlScreenContent(
  state: RecipeControlScreenState,
  onIntent: (RecipeControlScreenIntent) -> Unit,
  pagerState: PagerState = rememberPagerState(),
  pages: Array<RecipeControlScreenPage> = RecipeControlScreenPage.values(),
) {
  val scope = rememberCoroutineScope()

  val colors = LocalTheme.colors

  val recipe = state.recipe

  Column(
    modifier = Modifier
      .background(color = colors.backgroundSecondary)
      .navigationBarsPadding()
      .padding(horizontal = 12.dp)
      .animateContentSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    BottomSheetSlider()

    recipe?.let {
      HorizontalPager(
        count = pages.size,
        state = pagerState,
        userScrollEnabled = false,
      ) { index ->
        when (pages[index]) {
          RecipeControlScreenPage.MENU -> RecipeControlScreenMenu(
            recipe = recipe,
            onIntent = onIntent,
          )
          RecipeControlScreenPage.CATEGORIES_SELECTION -> RecipeCategoriesSelectionBlock(
            recipe = recipe,
            navigator = object : IBaseNavigator {
              override fun navigateUp(skipAnimation: Boolean) {
                scope.launch {
                  pagerState.animateScrollToPage(pages.indexOf(RecipeControlScreenPage.MENU))
                }
              }
            }
          )
        }
      }
    }
  }
}