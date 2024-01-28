package io.chefbook.features.recipe.control.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.padding
import io.chefbook.features.recipe.control.ui.components.categories.RecipeCategoriesSelectionBlock
import io.chefbook.features.recipe.control.ui.components.menu.RecipeControlScreenMenu
import io.chefbook.features.recipe.control.ui.mvi.RecipeControlScreenIntent
import io.chefbook.features.recipe.control.ui.mvi.RecipeControlScreenState
import io.chefbook.features.recipe.control.ui.state.RecipeControlScreenPage
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.bottomsheet.BottomSheetSlider
import io.chefbook.design.theme.shapes.ModalBottomSheetShape
import io.chefbook.navigation.navigators.BaseNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun RecipeControlScreenContent(
  state: RecipeControlScreenState,
  onIntent: (RecipeControlScreenIntent) -> Unit,
  pages: Array<RecipeControlScreenPage> = RecipeControlScreenPage.entries.toTypedArray(),
  pagerState: PagerState = rememberPagerState { pages.size },
) {
  val scope = rememberCoroutineScope()

  val colors = LocalTheme.colors

  val recipe = state.recipe

  Column(
    modifier = Modifier
      .background(colors.backgroundSecondary, ModalBottomSheetShape)
      .navigationBarsPadding()
      .padding(horizontal = 12.dp)
      .animateContentSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    BottomSheetSlider()

    recipe?.let {
      HorizontalPager(
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
            navigator = object : BaseNavigator {
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