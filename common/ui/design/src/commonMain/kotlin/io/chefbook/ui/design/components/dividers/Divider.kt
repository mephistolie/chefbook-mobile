package io.chefbook.ui.design.components.dividers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import io.chefbook.ui.design.theme.dimens.DividerHeight

@Composable
fun Divider(
  modifier: Modifier = Modifier,
  height: Dp = DividerHeight,
  color: Color = LocalTheme.colors.backgroundSecondary,
) {
  HorizontalDivider(
    modifier = modifier.fillMaxWidth(),
    thickness = height,
    color = color,
  )
}
