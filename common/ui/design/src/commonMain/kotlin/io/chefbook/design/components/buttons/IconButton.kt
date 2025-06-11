package io.chefbook.design.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import io.chefbook.core.compose.constants.ShortDebounceInterval
import io.chefbook.core.compose.modifiers.clickable.composite.hoverScaleIndication
import io.chefbook.design.theme.shapes.NoPaddings

@Composable
fun IconButton(
  icon: ImageVector,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  shape: Shape = CircleShape,
  colors: StandardButtonColors = standardButtonColors(),
  border: BorderStroke? = null,
  contentPadding: PaddingValues = NoPaddings,
  interactionSource: MutableInteractionSource? = null,
  indication: Indication? = hoverScaleIndication(),
  debounceInterval: Long? = ShortDebounceInterval,
  contentDescription: String? = null
) =
  StandardButton(
    onClick = onClick,
    modifier = modifier.aspectRatio(1F),
    enabled = enabled,
    shape = shape,
    colors = colors,
    border = border,
    contentPadding = contentPadding,
    interactionSource = interactionSource,
    indication = indication,
    debounceInterval = debounceInterval,
  ) {
    Icon(
      imageVector = icon,
      contentDescription = contentDescription,
      tint = LocalContentColor.current,
    )
  }
