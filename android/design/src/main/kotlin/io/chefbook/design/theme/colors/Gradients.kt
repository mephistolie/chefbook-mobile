package io.chefbook.design.theme.colors

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import io.chefbook.core.android.compose.providers.theme.LocalTheme

object Gradients {
  object Premium {
    val startColor = Color(0xFFFDC830)
    val endColor = Color(0xFFF37335)
  }

  val orangeBrush = Brush.linearGradient(
    colors = listOf(
      Premium.startColor,
      Premium.endColor,
    )
  )

  @Composable
  fun grayBrush() = Brush.linearGradient(
    colors = listOf(
      LocalTheme.colors.backgroundSecondary,
      LocalTheme.colors.backgroundTertiary,
    )
  )
}
