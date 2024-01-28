import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme

@Composable
fun Switch(
  isChecked: Boolean,
) {
  val density = LocalDensity.current

  val colors = LocalTheme.colors

  val width = 40.dp
  val height = 24.dp
  val gap = 3.dp

  val thumbRadius = (height / 2) - gap

  val animatePosition = animateFloatAsState(
    label = "switch",
    targetValue = with(density) {
      if (isChecked)
        (width - thumbRadius - gap).toPx()
      else
        (thumbRadius + gap).toPx()
    }
  )

  Canvas(
    modifier = Modifier.size(width = width, height = height)
  ) {
    drawRoundRect(
      color = if (isChecked) colors.tintPrimary else colors.backgroundTertiary,
      cornerRadius = CornerRadius(x = 28.dp.toPx(), y = 28.dp.toPx()),
    )

    drawCircle(
      color = colors.backgroundPrimary,
      radius = thumbRadius.toPx(),
      center = Offset(
        x = animatePosition.value,
        y = size.height / 2
      )
    )
  }
}
