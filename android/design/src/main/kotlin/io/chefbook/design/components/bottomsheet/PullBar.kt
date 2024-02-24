package io.chefbook.design.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape4

@Composable
fun PullBar(
  modifier: Modifier = Modifier,
  isInverted: Boolean = false,
) {
  Box(
    modifier = modifier
      .padding(vertical = 6.dp)
      .width(32.dp)
      .height(4.dp)
      .background(
        color = if (isInverted) {
          LocalTheme.colors.backgroundPrimary.copy(alpha = 0.6F)
        } else {
          LocalTheme.colors.foregroundPrimary.copy(alpha = 0.2F)
        },
        shape = RoundedCornerShape4
      )
  )
}

enum class PullBarType {
  NONE, INTERNAL, EXTERNAl
}
