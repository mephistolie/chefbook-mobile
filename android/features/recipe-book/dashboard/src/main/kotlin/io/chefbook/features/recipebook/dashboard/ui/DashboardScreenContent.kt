package io.chefbook.features.recipebook.dashboard.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.modifiers.shimmer
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape28Bottom
import io.chefbook.design.theme.shapes.RoundedCornerShape28Top
import io.chefbook.features.recipebook.dashboard.ui.components.blocks.ALL_RECIPES_CARD_KEY_PREFIX
import io.chefbook.features.recipebook.dashboard.ui.components.blocks.DashboardTopBar
import io.chefbook.features.recipebook.dashboard.ui.components.blocks.RecipeBookOnlineBlock
import io.chefbook.features.recipebook.dashboard.ui.components.blocks.allRecipesBlock
import io.chefbook.features.recipebook.dashboard.ui.components.blocks.bottombar.BottomBar
import io.chefbook.features.recipebook.dashboard.ui.components.blocks.bottombar.bottomBarHeight
import io.chefbook.features.recipebook.dashboard.ui.components.blocks.categoriesBlock
import io.chefbook.features.recipebook.dashboard.ui.components.blocks.quickAccessBlock
import io.chefbook.features.recipebook.dashboard.ui.mvi.ContentAppearance
import io.chefbook.features.recipebook.dashboard.ui.mvi.DashboardScreenIntent
import io.chefbook.features.recipebook.dashboard.ui.mvi.DashboardScreenState
import io.chefbook.sdk.encryption.vault.api.external.domain.entities.EncryptedVaultState
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun DashboardScreenContent(
  state: DashboardScreenState,
  onIntent: (DashboardScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val contentGridState = rememberLazyGridState()
  val scope = rememberCoroutineScope()

  val isAllRecipesBlockExpanded = remember(contentGridState) {
    derivedStateOf {
      contentGridState.layoutInfo.visibleItemsInfo.firstOrNull()?.key.toString()
        .contains(ALL_RECIPES_CARD_KEY_PREFIX)
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(colors.backgroundPrimary)
      .systemBarsPadding(),
  ) {
    LazyVerticalGrid(
      columns = GridCells.Fixed(4),
      state = contentGridState,
      modifier = Modifier
        .padding(bottom = bottomBarHeight)
        .wrapContentHeight()
        .animateContentSize(),
    ) {
      item(span = { GridItemSpan(4) }) {
        DashboardTopBar(
          modifier = Modifier
            .statusBarsPadding()
            .padding(20.dp, 4.dp, 20.dp),
          avatar = state.profileAvatar,
          onIntent = onIntent,
        )
      }
      item(span = { GridItemSpan(4) }) {
        AnimatedVisibility(visible = state.onlineFeaturesAppearance != ContentAppearance.HIDDEN) {
          RecipeBookOnlineBlock(
            modifier = Modifier
              .background(colors.divider)
              .padding(bottom = 8.dp)
              .background(colors.backgroundPrimary, RoundedCornerShape28Bottom)
              .padding(12.dp, 20.dp, 12.dp, 16.dp)
              .pointerInteropFilter { state.onlineFeaturesAppearance != ContentAppearance.SHOWN }
              .shimmer(isEnabled = state.onlineFeaturesAppearance == ContentAppearance.SHIMMERING),
            onCommunityRecipesButtonClick = { onIntent(DashboardScreenIntent.OpenCommunityRecipes) },
            onEncryptedVaultButtonClick = { onIntent(DashboardScreenIntent.OpenEncryptionMenu) },
            encryption = state.encryption ?: EncryptedVaultState.DISABLED,
          )
        }
      }
      quickAccessBlock(
        recipes = state.latestRecipes,
        drawDivider = state.onlineFeaturesAppearance != ContentAppearance.HIDDEN,
        onRecipeClicked = { id -> onIntent(DashboardScreenIntent.OpenRecipe(id)) },
      )
      categoriesBlock(
        categories = state.categories,
        drawDivider = state.onlineFeaturesAppearance != ContentAppearance.HIDDEN && state.latestRecipes?.isEmpty() == true,
        onCategoryClicked = { id -> onIntent(DashboardScreenIntent.OpenCategory(id)) },
        onCategoriesClick = { onIntent(DashboardScreenIntent.OpenCategories) },
      )
      allRecipesBlock(
        recipes = state.allRecipes,
        onRecipeClicked = { id -> onIntent(DashboardScreenIntent.OpenRecipe(id)) },
      )
    }

    BottomBar(
      modifier = Modifier.align(Alignment.BottomCenter),
      isScrollUpButtonVisible = isAllRecipesBlockExpanded.value,
      onScrollUpButtonClick = { scope.launch { contentGridState.animateScrollToItem(0) } },
      onShoppingListIconClick = { onIntent(DashboardScreenIntent.OpenShoppingListScreen) },
      onSearchFieldClick = { onIntent(DashboardScreenIntent.OpenRecipeSearch) },
      onFavouriteButtonClick = { onIntent(DashboardScreenIntent.OpenFavouriteRecipes) },
    )
  }
}
