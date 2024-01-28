package io.chefbook.features.recipebook.dashboard.ui.components.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.android.compose.modifiers.shimmer
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape12
import io.chefbook.design.theme.shapes.RoundedCornerShape8

@Composable
internal fun LatestRecipeCardSkeleton() {
  val colors = LocalTheme.colors

  Row(
    modifier = Modifier
      .padding(end = 12.dp)
      .height(80.dp)
      .aspectRatio(2.25F)
      .shimmer()
  ) {
    Box(
      modifier = Modifier
        .padding(end = 8.dp)
        .size(80.dp)
        .clippedBackground(colors.backgroundTertiary, RoundedCornerShape12)
    )
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween,
    ) {
      Column {
        Box(
          modifier = Modifier
            .padding(bottom = 6.dp)
            .width(72.dp)
            .height(8.dp)
            .clippedBackground(colors.backgroundTertiary, RoundedCornerShape(7.dp))
        )
        Box(
          modifier = Modifier
            .width(44.dp)
            .height(8.dp)
            .clippedBackground(colors.backgroundTertiary, RoundedCornerShape(5.dp))
        )
      }
      Box(
        modifier = Modifier
          .width(72.dp)
          .height(28.dp)
          .clippedBackground(colors.backgroundTertiary, RoundedCornerShape8),
      )
    }
  }
}
