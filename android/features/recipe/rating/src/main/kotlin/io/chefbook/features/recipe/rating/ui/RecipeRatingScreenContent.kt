package io.chefbook.features.recipe.rating.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.padding
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.bottomsheet.BottomSheetColumn
import io.chefbook.design.components.bottomsheet.PullBarType
import io.chefbook.design.theme.shapes.RoundedCornerShape28Top
import io.chefbook.design.theme.shapes.RoundedCornerShape28
import io.chefbook.features.recipe.rating.ui.components.RecipeRatingBlock
import io.chefbook.features.recipe.rating.ui.components.RecipeScoreBlock
import io.chefbook.features.recipe.rating.ui.mvi.RecipeRatingScreenIntent
import io.chefbook.features.recipe.rating.ui.mvi.RecipeRatingScreenState

@Composable
internal fun RecipeRatingScreenContent(
  state: RecipeRatingScreenState,
  onIntent: (RecipeRatingScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors

  var ratingBlockModifier = Modifier
    .background(
      color = colors.backgroundPrimary,
      shape = if (state.isScoreVisible) RoundedCornerShape28 else RoundedCornerShape28Top,
    )
  if (!state.isScoreVisible) ratingBlockModifier = ratingBlockModifier.navigationBarsPadding()

  BottomSheetColumn(
    backgroundColor = colors.divider,
    pullBarType = PullBarType.EXTERNAl
  ) {
    RecipeRatingBlock(
      rating = state.rating,
      modifier = ratingBlockModifier,
    )
    AnimatedVisibility(state.isScoreVisible) {
      RecipeScoreBlock(
        score = state.rating.score,
        onScoreClick = { onIntent(RecipeRatingScreenIntent.ScoreClicked(it)) },
        modifier = Modifier.padding(top = 8.dp),
      )
    }
  }
}
