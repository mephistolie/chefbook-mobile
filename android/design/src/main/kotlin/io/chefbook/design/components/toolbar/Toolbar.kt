package io.chefbook.design.components.toolbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.icons.ArrowStart
import io.chefbook.design.icons.ChefBookIcons
import io.chefbook.design.theme.dimens.IconSize24
import io.chefbook.design.theme.dimens.ToolbarHeight

@Composable
fun Toolbar(
  modifier: Modifier = Modifier,
  leftButtonIcon: ImageVector? = ChefBookIcons.ArrowStart,
  onLeftButtonClick: () -> Unit = {},
  rightButtonIcon: ImageVector? = null,
  rightButtonTint: Color = LocalTheme.colors.foregroundPrimary,
  rightIconEndPadding: Dp = 0.dp,
  onRightButtonClick: () -> Unit = {},
  onContentClick: () -> Unit = {},
  contentAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
  content: @Composable ColumnScope.() -> Unit
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .height(ToolbarHeight)
      .padding(vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    leftButtonIcon?.let {
      ToolbarIcon(
        icon = leftButtonIcon,
        onClick = onLeftButtonClick,
        paddingEnd = 12.dp,
      )
    }
    Column(
      modifier = Modifier
        .padding(
          start = if (rightButtonIcon != null && leftButtonIcon == null) 36.dp else 0.dp,
          end = if (leftButtonIcon != null && rightButtonIcon == null) 36.dp else 0.dp,
        )
        .weight(1F)
        .fillMaxWidth()
        .simpleClickable(onClick = onContentClick),
      horizontalAlignment = contentAlignment,
      content = content
    )
    rightButtonIcon?.let {
      ToolbarIcon(
        icon = rightButtonIcon,
        onClick = onRightButtonClick,
        tint = rightButtonTint,
        paddingStart = 12.dp,
        iconPaddingEnd = rightIconEndPadding,
      )
    }
  }
}

@Composable
fun ToolbarIcon(
  icon: ImageVector,
  onClick: () -> Unit,
  tint: Color = LocalTheme.colors.foregroundPrimary,
  paddingStart: Dp = 0.dp,
  paddingEnd: Dp = 0.dp,
  iconPaddingEnd: Dp = 0.dp,
) {
  Icon(
    imageVector = icon,
    tint = tint,
    modifier = Modifier
      .padding(start = paddingStart, end = paddingEnd)
      .size(IconSize24)
      .padding(end = iconPaddingEnd)
      .simpleClickable(onClick = onClick),
    contentDescription = null,
  )
}
