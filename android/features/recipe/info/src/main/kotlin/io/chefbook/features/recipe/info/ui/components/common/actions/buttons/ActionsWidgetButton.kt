package io.chefbook.features.recipe.info.ui.components.common.actions.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton

@Composable
internal fun ActionsWidgetButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  text: String? = null,
  @DrawableRes
  leftIconId: Int? = null,
  @DrawableRes
  rightIconId: Int? = null,
  rightIconModifier: Modifier = Modifier,
  isSelected: Boolean = false,
) {
  val colors = LocalTheme.colors

  DynamicButton(
    onClick = onClick,
    modifier = modifier.height(50.dp),
    horizontalPadding = 14.dp,
    text = text,
    leftIcon = leftIconId?.let { ImageVector.vectorResource(it) },
    rightIcon = rightIconId?.let { ImageVector.vectorResource(it) },
    rightIconModifier = rightIconModifier,
    iconsSize = 18.dp,
    isSelected = isSelected,
    unselectedBackground = colors.backgroundTertiary,
    unselectedForeground = colors.foregroundPrimary,
  )
}
