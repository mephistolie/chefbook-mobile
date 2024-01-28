package io.chefbook.design.components.toolbar

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.R
import io.chefbook.design.theme.dimens.DefaultIconSize
import io.chefbook.design.theme.dimens.ToolbarHeight

@Composable
fun Toolbar(
  modifier: Modifier = Modifier,
  @DrawableRes
  leftButtonIconId: Int? = R.drawable.ic_arrow_left,
  onLeftButtonClick: () -> Unit = {},
  @DrawableRes
  rightButtonIconId: Int? = null,
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
    leftButtonIconId?.let {
      ToolbarIcon(
        iconId = leftButtonIconId,
        onClick = onLeftButtonClick,
        paddingEnd = 12.dp,
      )
    }
    Column(
      modifier = Modifier
        .padding(
          start = if (rightButtonIconId != null && leftButtonIconId == null) 36.dp else 0.dp,
          end = if (leftButtonIconId != null && rightButtonIconId == null) 36.dp else 0.dp,
        )
        .weight(1F)
        .fillMaxWidth()
        .simpleClickable(onClick = onContentClick),
      horizontalAlignment = contentAlignment,
      content = content
    )
    rightButtonIconId?.let {
      ToolbarIcon(
        iconId = rightButtonIconId,
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
  @DrawableRes
  iconId: Int,
  onClick: () -> Unit,
  tint: Color = LocalTheme.colors.foregroundPrimary,
  paddingStart: Dp = 0.dp,
  paddingEnd: Dp = 0.dp,
  iconPaddingEnd: Dp = 0.dp,
) {
  Icon(
    imageVector = ImageVector.vectorResource(iconId),
    tint = tint,
    modifier = Modifier
      .padding(start = paddingStart, end = paddingEnd)
      .size(DefaultIconSize)
      .padding(end = iconPaddingEnd)
      .simpleClickable(onClick = onClick),
    contentDescription = null,
  )
}
