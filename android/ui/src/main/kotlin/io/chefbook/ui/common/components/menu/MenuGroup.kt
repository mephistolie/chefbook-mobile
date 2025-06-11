package io.chefbook.ui.common.components.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.core.compose.modifiers.clippingBackground
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.smooth.SmoothCornerShape

private val cornerRadius = 24.dp

@Composable
fun MenuGroup(
  modifier: Modifier = Modifier,
  isFirst: Boolean = false,
  isLast: Boolean = false,
  content: @Composable ColumnScope.() -> Unit,
) {
  val colors = LocalTheme.colors

  Column(
    modifier = modifier
      .run { if (isLast) fillMaxSize() else fillMaxWidth() }
      .clippingBackground(
        color = colors.backgroundPrimary,
        shape = SmoothCornerShape(
          topStart = if (isFirst) 0.dp else cornerRadius,
          topEnd = if (isFirst) 0.dp else cornerRadius,
          bottomStart = if (isLast) 0.dp else cornerRadius,
          bottomEnd = if (isLast) 0.dp else cornerRadius,
        )
      )
      .padding(
        start = 16.dp,
        top = if (isFirst) 0.dp else 10.dp,
        end = 16.dp,
        bottom = 10.dp,
      )
      .run { if (isLast) navigationBarsPadding() else this },
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    content()
  }
}
