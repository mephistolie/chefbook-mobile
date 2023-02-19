package com.mysty.chefbook.features.recipe.control.ui.components.menu

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.padding
import com.mephistolie.compost.modifiers.scalingClickable
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme

@Composable
internal fun RecipeActionButton(
  onClick: () -> Unit,
  text: String,
  textTint: Color = LocalTheme.colors.foregroundPrimary,
  @DrawableRes
  iconId: Int? = null,
  iconSize: Dp = 20.dp,
  iconTint: Color = textTint,
  isFirst: Boolean = false,
  isLast: Boolean = false,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val pressed = remember { mutableStateOf(false) }

  Box(modifier = Modifier
    .padding(
      top = if (isFirst) 8.dp else 0.dp,
      bottom = if (isLast) 8.dp else 0.dp,
    )
    .clippedBackground(
      background = colors.backgroundPrimary,
      shape = RoundedCornerShape(
        if (isFirst) 16.dp else 0.dp,
        if (isFirst) 16.dp else 0.dp,
        if (isLast) 16.dp else 0.dp,
        if (isLast) 16.dp else 0.dp,
      )
    )
    .scalingClickable(
      pressed = pressed,
      scaleFactor = 1F,
      debounceInterval = 500L,
      onClick = onClick,
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp, horizontal = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = text,
        style = typography.headline1,
        color = textTint,
      )
      iconId?.let {
        Icon(
          imageVector = ImageVector.vectorResource(iconId),
          tint = iconTint,
          modifier = Modifier.size(iconSize),
          contentDescription = null,
        )
      }
    }
    Shading(pressed.value)
  }
  if (!isLast) Divider(color = colors.backgroundTertiary)
}