package io.chefbook.ui.design.components.buttons

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ButtonIcon(
  imageVector: ImageVector?,
  modifier: Modifier = Modifier,
) {
  imageVector?.let {
    Icon(
      imageVector = imageVector,
      tint = LocalContentColor.current,
      modifier = modifier,
      contentDescription = null,
    )
  }
}
