package io.chefbook.features.recipe.rating.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.padding
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.bottomsheet.BottomSheetSlider
import io.chefbook.design.core.scorePainter
import io.chefbook.features.recipe.rating.R
import io.chefbook.design.R as designR
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta

@Composable
internal fun RecipeRatingBlock(
  rating: RecipeMeta.Rating,
  modifier: Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = modifier.padding(horizontal = 16.dp, top = 0.dp, bottom = 20.dp),
  ) {
    BottomSheetSlider(modifier = Modifier.align(Alignment.CenterHorizontally))
    Text(
      text = stringResource(R.string.common_recipe_rating_screen_community_rating),
      style = typography.h2,
      color = colors.foregroundPrimary,
      modifier = Modifier
        .padding(top = 8.dp, bottom = 16.dp)
        .wrapContentHeight()
    )
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Image(
        painter = scorePainter(rating.index, rating.votes),
        contentDescription = null,
        modifier = Modifier.size(48.dp),
      )
      Column {
        Text(
          text = if (rating.votes > 0) "${rating.index}" else "–",
          style = typography.h2,
          color = colors.foregroundPrimary,
        )
        Text(
          text = "${stringResource(R.string.common_recipe_rating_screen_scores)}: ${rating.votes}",
          style = typography.subhead1,
          color = colors.foregroundSecondary,
        )
      }
    }
  }
}
