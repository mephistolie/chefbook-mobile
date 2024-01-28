package io.chefbook.features.settings.ui.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape16

private val transparentBrush = Brush.linearGradient(
  colors = listOf(
    Color.Transparent,
    Color.Transparent,
  )
)

@Composable
fun MenuSelectableIcon(
  icon: ImageVector,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  isSelected: Boolean = false,
) {
  val colors = LocalTheme.colors

  MenuSelectable(
    modifier = modifier,
    onClick = onClick,
    isSelected = isSelected,
  ) {
    Icon(
      imageVector = icon,
      modifier = Modifier.size(28.dp),
      tint = colors.foregroundPrimary,
      contentDescription = null,
    )
  }
}

@Composable
fun MenuSelectableImage(
  image: ImageVector,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  isSelected: Boolean = false,
) {
  MenuSelectable(
    modifier = modifier,
    onClick = onClick,
    isSelected = isSelected,
  ) {
    Image(
      imageVector = image,
      modifier = Modifier.size(40.dp),
      contentDescription = null,
    )
  }
}

@Composable
private fun MenuSelectable(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  isSelected: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colors = LocalTheme.colors

  val focusedTransition = updateTransition(isSelected, label = "isSelected")
  val borderDp by focusedTransition.animateDp(label = "backgroundColor") { selected ->
      if (selected) 2.dp else 0.dp
  }

  Box(
    modifier = modifier
      .size(64.dp)
      .border(
        width = borderDp,
        color = if (isSelected) colors.tintPrimary else Color.Transparent,
        shape = RoundedCornerShape16
      )
      .border(
        width = if (isSelected) 3.dp else 0.dp,
        color = if (isSelected) colors.backgroundPrimary else colors.backgroundSecondary,
        shape = RoundedCornerShape16
      )
      .clippedBackground(
        background = colors.backgroundSecondary,
        shape = RoundedCornerShape16,
      )
      .simpleClickable(onClick = onClick),
    contentAlignment = Alignment.Center,
  ) {
    content()
  }
}
