package io.chefbook.features.recipe.info.ui.components.common.content.loaded

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.chefbook.features.recipe.info.ui.components.cooking.CookingPage
import io.chefbook.features.recipe.info.ui.components.ingredients.IngredientsPage
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import io.chefbook.ui.common.presentation.RecipeScreenPage

val pages = RecipeScreenPage.entries.toTypedArray()

@OptIn(ExperimentalFoundationApi::class)
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

  HorizontalPager(
    state = pagerState,
    modifier = Modifier
      .animateContentSize()
      .wrapContentHeight(),
    verticalAlignment = Alignment.Top,
    flingBehavior = PagerDefaults.flingBehavior(state = pagerState)
  ) { pageIndex ->
    val page = pages[pageIndex]
    val scrolling by remember(pagerState) { derivedStateOf { pagerState.currentPageOffsetFraction != 0f } }

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
