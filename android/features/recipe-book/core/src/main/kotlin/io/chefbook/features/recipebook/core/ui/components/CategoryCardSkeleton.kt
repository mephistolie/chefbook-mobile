package io.chefbook.features.recipebook.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.android.compose.modifiers.shimmer
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape16
import io.chefbook.design.theme.shapes.RoundedCornerShape28
import io.chefbook.design.theme.shapes.RoundedCornerShape4

@Composable
fun CategoryCardSkeleton(
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors

  Box(
    modifier = modifier
      .padding(0.dp, 0.dp, 0.dp, 8.dp)
      .aspectRatio(0.8f)
      .clippedBackground(colors.backgroundSecondary, RoundedCornerShape16)
      .shimmer()
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceAround,
    ) {
      Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
          .fillMaxSize()
          .weight(1F)
          .padding(12.dp, 12.dp, 12.dp),
      ) {
        Box(
          modifier = Modifier
            .width(50.dp)
            .height(6.dp)
            .clippedBackground(colors.backgroundTertiary, RoundedCornerShape4)
        )
        Box(
          modifier = Modifier
            .padding(top = 4.dp)
            .width(30.dp)
            .height(6.dp)
            .clippedBackground(colors.backgroundTertiary, RoundedCornerShape4)
        )
      }
      Box(
        modifier = Modifier
          .size(56.dp)
          .offset(18.dp, 6.dp)
          .clippedBackground(colors.backgroundTertiary, RoundedCornerShape28),
      )
    }
  }
}
