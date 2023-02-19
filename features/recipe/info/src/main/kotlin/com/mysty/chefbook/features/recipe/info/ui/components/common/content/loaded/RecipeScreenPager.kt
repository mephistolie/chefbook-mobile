package com.mysty.chefbook.features.recipe.info.ui.components.common.content.loaded

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerDefaults
import com.google.accompanist.pager.PagerState
import com.mysty.chefbook.features.recipe.info.ui.components.cooking.CookingPage
import com.mysty.chefbook.features.recipe.info.ui.components.ingredients.IngredientsPage
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import com.mysty.chefbook.ui.common.presentation.RecipeScreenPage
import dev.chrisbanes.snapper.ExperimentalSnapperApi

@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
internal fun RecipeScreenPager(
  state: RecipeScreenState.Success,
  onServingsChanged: (Int) -> Unit,
  onIngredientClick: (String) -> Unit,
  onStepPictureClicked: (String) -> Unit,
  pagerState: PagerState,
  pageHeight: Dp,
) {
  val recipe = state.recipe

  val pages = remember { RecipeScreenPage.values() }

  HorizontalPager(
    count = pages.size,
    state = pagerState,
    modifier = Modifier
      .animateContentSize()
      .wrapContentHeight(),
    verticalAlignment = Alignment.Top,
    flingBehavior = PagerDefaults.flingBehavior(
      state = pagerState,
      endContentPadding = 0.dp
    )
  ) { pageIndex ->
    val page = pages[pageIndex]
    val scrolling by remember(pagerState) { derivedStateOf { pagerState.currentPageOffset != 0f } }

    val heightModifier = if (pagerState.currentPage == pageIndex || scrolling) {
      Modifier.heightIn(min = pageHeight)
    } else {
      Modifier.requiredHeightIn(max = pageHeight)
    }

    Box(
      contentAlignment = Alignment.TopCenter,
      modifier = heightModifier
    ) {
      when (page) {
        RecipeScreenPage.INGREDIENTS -> IngredientsPage(
          ingredients = recipe.ingredients,
          selectedIngredients = state.selectedIngredients,
          servings = recipe.servings,
          servingsMultiplier = state.servingsMultiplier,
          onServingsChanged = onServingsChanged,
          onIngredientClick = onIngredientClick,
        )
        RecipeScreenPage.COOKING -> CookingPage(
          cooking = recipe.cooking,
          time = recipe.time,
          onStepPictureClicked = onStepPictureClicked,
        )
      }
    }
  }
}
