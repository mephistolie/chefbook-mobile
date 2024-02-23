package io.chefbook.features.recipe.rating.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.BlendModeColorFilter
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.padding
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.R as coreR
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.core.scorePainter
import io.chefbook.design.theme.shapes.BottomSheetShape
import io.chefbook.features.recipe.rating.R
import io.chefbook.design.R as designR

@Composable
internal fun RecipeScoreBlock(
  score: Int?,
  onScoreClick: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(colors.backgroundPrimary, BottomSheetShape)
      .navigationBarsPadding()
      .padding(12.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = stringResource(R.string.common_recipe_rating_screen_your_score),
      style = typography.h2,
      color = colors.foregroundPrimary,
      modifier = Modifier
        .align(Alignment.Start)
        .padding(horizontal = 4.dp, top = 4.dp, bottom = 16.dp)
        .wrapContentHeight()
    )
    Row(
      modifier = Modifier.padding(top = 8.dp, horizontal = 16.dp, bottom = 24.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
      repeat(5) { index ->
        val star = index + 1

        val opacity = animateFloatAsState(
          targetValue = if (score == null || star > score) 1F else 0F,
          label = "star_${index}_saturation"
        )

        Image(
          painter = scorePainter(star),
          contentDescription = null,
          modifier = Modifier
            .weight(1F)
            .fillMaxWidth()
            .aspectRatio(1F)
            .simpleClickable(300L) { onScoreClick(star) },
          colorFilter = ColorFilter.tint(colors.backgroundTertiary.copy(alpha = opacity.value), BlendMode.SrcAtop),
        )
      }
    }
  }
}
