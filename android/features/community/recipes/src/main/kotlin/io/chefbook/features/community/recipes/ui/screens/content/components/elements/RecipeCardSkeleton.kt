package io.chefbook.features.community.recipes.ui.screens.content.components.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.android.compose.modifiers.shimmer
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape12
import io.chefbook.design.theme.shapes.RoundedCornerShape16

@Composable
fun RecipeCardSkeleton(
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors

  Column(
    modifier = modifier
      .fillMaxWidth()
      .shimmer()
  ) {
    Box(
      modifier = Modifier
        .padding(bottom = 10.dp)
        .aspectRatio(1F)
        .clippedBackground(colors.backgroundSecondary, RoundedCornerShape16)
    )
    Box(
      modifier = Modifier
        .padding(bottom = 8.dp)
        .width(144.dp)
        .height(12.dp)
        .clippedBackground(colors.backgroundSecondary, RoundedCornerShape12)
    )
    Box(
      modifier = Modifier
        .padding(bottom = 2.dp)
        .width(72.dp)
        .height(10.dp)
        .clippedBackground(colors.backgroundSecondary, RoundedCornerShape12)
    )
  }
}
