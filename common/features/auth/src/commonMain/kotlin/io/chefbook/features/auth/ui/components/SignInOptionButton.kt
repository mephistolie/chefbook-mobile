package io.chefbook.features.auth.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.chefbook.ui.design.components.buttons.IconButton

@Composable
fun SignInOptionButton(
  icon: ImageVector,
  onClick: () -> Unit,
) {
  IconButton(
    icon = icon,
    onClick = onClick,
    modifier = Modifier
      .size(52.dp)
      .padding(5.dp),
  )
}
