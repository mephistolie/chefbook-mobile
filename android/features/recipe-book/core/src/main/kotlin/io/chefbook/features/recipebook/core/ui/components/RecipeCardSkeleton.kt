package io.chefbook.features.recipebook.core.ui.components

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
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.SmoothCornerShape16
import io.chefbook.design.theme.shapes.smooth.SmoothCornerShape

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
        .padding(bottom = 8.dp)
        .aspectRatio(1F)
        .clippedBackground(colors.backgroundSecondary, SmoothCornerShape16)
    )
    Box(
      modifier = Modifier
        .padding(bottom = 6.dp)
        .width(144.dp)
        .height(10.dp)
        .clippedBackground(colors.backgroundSecondary, SmoothCornerShape(7.dp))
    )
    Box(
      modifier = Modifier
        .width(72.dp)
        .height(8.dp)
        .clippedBackground(colors.backgroundSecondary, SmoothCornerShape(5.dp))
    )
  }
}
