package io.chefbook.design.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme

@Composable
fun CircleIconButton(
  @DrawableRes iconId: Int,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  iconModifier: Modifier = Modifier,
  enabled: Boolean = true,
  contentPadding: Dp = 0.dp,
  tint: Color = LocalTheme.colors.foregroundPrimary,
  colors: ButtonColors = ButtonDefaults.buttonColors(backgroundColor = LocalTheme.colors.backgroundSecondary),
  contentDescription: String? = null
) =
  com.mephistolie.compost.ui.buttons.CircleIconButton(
    icon = ImageVector.vectorResource(iconId),
    onClick = onClick,
    enabled = enabled,
    modifier = modifier,
    iconModifier = iconModifier,
    contentPadding = contentPadding,
    tint = tint,
    colors = colors,
    contentDescription = contentDescription,
  )
