package io.chefbook.features.community.recipes.ui.screens.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.dimens.ToolbarHeight
import io.chefbook.features.community.recipes.ui.screens.content.components.blocks.FilterButtons
import io.chefbook.features.community.recipes.ui.screens.content.components.blocks.RECIPE_CARD_KEY_PREFIX
import io.chefbook.features.community.recipes.ui.screens.content.components.blocks.Tabs
import io.chefbook.features.community.recipes.ui.screens.content.components.blocks.Toolbar
import io.chefbook.features.community.recipes.ui.screens.content.components.blocks.recipesBlock
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenIntent
import io.chefbook.features.community.recipes.ui.mvi.CommunityRecipesScreenState
import io.chefbook.features.community.recipes.ui.screens.content.components.blocks.FilterBlock
import io.chefbook.features.community.recipes.ui.screens.content.components.blocks.filterBlockHeight
import kotlinx.coroutines.launch

@Composable
internal fun CommunityRecipesScreenContent(
  state: CommunityRecipesScreenState,
  onIntent: (CommunityRecipesScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(colors.backgroundPrimary)
      .statusBarsPadding(),
  ) {
    val scope = rememberCoroutineScope()

    val gridState = rememberLazyGridState()

    val isRecipesBlockExpanded = remember(gridState) {
      derivedStateOf {
        gridState.layoutInfo.visibleItemsInfo.firstOrNull()?.key.toString()
          .contains(RECIPE_CARD_KEY_PREFIX)
      }
    }

    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      state = gridState,
      modifier = Modifier.fillMaxSize(),
    ) {
      item(span = { GridItemSpan(2) }) { Spacer(Modifier.height(ToolbarHeight + 12.dp)) }
      item(span = { GridItemSpan(2) }) {
        AnimatedVisibility(
          visible = state.mode == CommunityRecipesScreenState.Mode.DASHBOARD,
          enter = fadeIn() + expandVertically(animationSpec = tween()),
          exit = fadeOut() + shrinkVertically(animationSpec = tween()),
        ) {
          Column(
            modifier = Modifier.background(colors.backgroundSecondary)
          ) {
            FilterButtons(
              tags = state.dashboard.tags,
              onSearchClick = { onIntent(CommunityRecipesScreenIntent.Content.SearchButtonClicked) },
              onFilterClick = { onIntent(CommunityRecipesScreenIntent.Content.FilterButtonClicked) },
              onTagClick = { onIntent(CommunityRecipesScreenIntent.Content.TagClicked(it.id)) },
              modifier = Modifier.clippedBackground(
                background = colors.backgroundPrimary,
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
              ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Tabs(
              selectedTab = state.dashboard.tab,
              onTabClick = { tab ->
                scope.launch {
                  gridState.animateScrollToItem(0)
                  onIntent(CommunityRecipesScreenIntent.Content.TabClicked(tab))
                }
              },
              modifier = Modifier.clippedBackground(
                background = colors.backgroundPrimary,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
              ),
            )
          }
        }
      }
      recipesBlock(
        recipes = state.recipes,
        isLoading = state.isLoading,
        offerCreateRecipe = state.mode == CommunityRecipesScreenState.Mode.DASHBOARD,
        onScrollEnded = { onIntent(CommunityRecipesScreenIntent.RecipesScrollEnded) },
        onRecipeClicked = { id -> onIntent(CommunityRecipesScreenIntent.RecipeCardClicked(id)) },
        onCreateRecipeClick = { onIntent(CommunityRecipesScreenIntent.Content.CreateRecipeClicked) },
      )
      item(span = { GridItemSpan(2) }) {
        Spacer(
          modifier = Modifier
            .navigationBarsPadding()
            .padding(bottom = if (state.mode == CommunityRecipesScreenState.Mode.FILTER) filterBlockHeight else 0.dp)
        )
      }
    }
    Toolbar(
      languages = state.languages,
      avatar = state.profileAvatar,
      isRecipesBlockExpanded = isRecipesBlockExpanded.value,
      mode = state.mode,
      tab = state.dashboard.tab,
      onBackClick = { onIntent(CommunityRecipesScreenIntent.Back) },
      onLanguageClick = { onIntent(CommunityRecipesScreenIntent.LanguagesButtonClicked) },
      onProfileClick = { onIntent(CommunityRecipesScreenIntent.ProfileButtonClicked) },
    )
    AnimatedVisibility(
      visible = state.mode == CommunityRecipesScreenState.Mode.FILTER,
      modifier = Modifier.align(Alignment.BottomCenter),
      enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween()),
      exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween()),
    ) {
      FilterBlock(
        filter = state.filter,
        onClick = { onIntent(CommunityRecipesScreenIntent.Content.FilterButtonClicked) },
        modifier = Modifier.align(Alignment.BottomCenter),
      )
    }
  }
}
