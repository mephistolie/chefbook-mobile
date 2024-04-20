package io.chefbook.ui.common.components.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.android.compose.providers.theme.LocalTheme

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
      .clippedBackground(
        background = colors.backgroundPrimary,
        shape = RoundedCornerShape(
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
