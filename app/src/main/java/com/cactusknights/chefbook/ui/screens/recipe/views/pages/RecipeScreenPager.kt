package com.cactusknights.chefbook.ui.screens.recipe.views.pages

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenState
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenTab
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerDefaults
import com.google.accompanist.pager.PagerState
import dev.chrisbanes.snapper.ExperimentalSnapperApi

@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun RecipeScreenPager(
    state: RecipeScreenState.Success,
    onCategoryClicked: (String) -> Unit,
    onChangeCategoriesClicked: () -> Unit,
    onCancelCategoriesSelectionClicked: () -> Unit,
    onConfirmCategoriesSelectionClicked: (List<String>) -> Unit,
    onAddToShoppingListClicked: (List<IngredientItem>, Float) -> Unit,
    onStepPictureClicked: (String) -> Unit,
    onEditRecipeClicked: () -> Unit,
    onDeleteRecipeClicked: () -> Unit,
    pages: List<RecipeScreenTab>,
    pagerState: PagerState,
    pageHeight: Dp,
) {
    val recipe = state.recipe

    HorizontalPager(
        count = pages.size,
        state = pagerState,
        modifier = Modifier
            .animateContentSize()
            .wrapContentHeight(),
        verticalAlignment = Alignment.Top,
        flingBehavior = PagerDefaults.flingBehavior(
            pagerState,
            endContentPadding = 0.dp
        )
    ) { pageIndex ->
        val tab = pages[pageIndex]
        val scrolling by remember(pagerState) {
            derivedStateOf {
                pagerState.currentPageOffset != 0f
            }
        }

        val heightModifier = if (pagerState.currentPage == pageIndex || scrolling) {
            Modifier.heightIn(min = pageHeight)
        } else {
            Modifier.requiredHeightIn(max = pageHeight)
        }
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = heightModifier
                .fillMaxWidth()
        ) {
            when (tab) {
                RecipeScreenTab.Details -> DetailsPage(
                    state = state,
                    onCategoryClicked = onCategoryClicked,
                    onChangeCategoriesClicked = onChangeCategoriesClicked,
                    onCancelCategoriesSelectionClicked = onCancelCategoriesSelectionClicked,
                    onConfirmCategoriesSelectionClicked = onConfirmCategoriesSelectionClicked,
                    onEditRecipeClicked = onEditRecipeClicked,
                    onDeleteRecipeClicked = onDeleteRecipeClicked,
                )
                RecipeScreenTab.Ingredients -> IngredientsPage(
                    ingredients = recipe.ingredients,
                    servings = recipe.servings,
                    onAddToShoppingListClicked = onAddToShoppingListClicked,
                )
                RecipeScreenTab.Cooking -> CookingPage(
                    cooking = recipe.cooking,
                    onStepPictureClicked = onStepPictureClicked,
                )
            }
        }
    }
}
