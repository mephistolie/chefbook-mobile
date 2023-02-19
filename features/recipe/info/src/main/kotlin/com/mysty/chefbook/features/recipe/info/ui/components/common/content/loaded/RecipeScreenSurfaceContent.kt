package com.mysty.chefbook.features.recipe.info.ui.components.common.content.loaded

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mysty.chefbook.design.components.buttons.BottomSheetCloseButton
import com.mysty.chefbook.features.recipe.info.ui.components.common.actions.ActionsWidget
import com.mysty.chefbook.features.recipe.info.ui.components.details.card.DetailsCard
import com.mysty.chefbook.features.recipe.info.ui.components.details.card.ImageCard
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenIntent
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import com.mysty.chefbook.ui.common.components.common.FlippingCard

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun RecipeScreenSurfaceContent(
  state: RecipeScreenState.Success,
  onIntent: (RecipeScreenIntent) -> Unit,
  contentHeight: MutableState<Dp>
) {
  val density = LocalDensity.current

  Box(
    modifier = Modifier.padding(horizontal = 8.dp),
    contentAlignment = Alignment.TopEnd
  ) {
    Column(
      modifier = Modifier
        .wrapContentHeight()
        .onGloballyPositioned { coordinates ->
          contentHeight.value = with(density) { coordinates.size.height.toDp() }
        }
    ) {
      val recipe = state.recipe
      val hasPreview = recipe.preview != null
      var isFrontShown by remember { mutableStateOf(hasPreview) }
      FlippingCard(
        isFrontShown = isFrontShown,
        onClick = { if (hasPreview) isFrontShown = !isFrontShown },
        modifier = Modifier
          .zIndex(24F)
          .aspectRatio(1F),
        frontContent = {
          recipe.preview?.let { preview ->
            ImageCard(url = preview, showFlipIcon = true)
          }
        },
        backContent = { DetailsCard(state = state, showFlipIcon = hasPreview) }
      )
      ActionsWidget(
        recipe = state.recipe,
        onLikeClick = { onIntent(RecipeScreenIntent.ChangeLikeStatus) },
        onSaveClick = {
          if (!state.recipe.isSaved) {
            onIntent(RecipeScreenIntent.AddToRecipeBook)
          } else {
            onIntent(RecipeScreenIntent.OpenRecipeMenu)
          }
        },
        onShareClick = { onIntent(RecipeScreenIntent.OpenShareDialog) },
        modifier = Modifier.padding(top = 12.dp, bottom = 24.dp),
      )
    }
    BottomSheetCloseButton(
      horizontalPadding = 8.dp,
      verticalPadding = 8.dp,
    ) { onIntent(RecipeScreenIntent.Close) }
  }
}
