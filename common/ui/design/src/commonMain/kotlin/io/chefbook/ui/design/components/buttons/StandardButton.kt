package io.chefbook.ui.design.components.buttons

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import io.chefbook.ui.utils.compose.constants.ShortDebounceInterval
import io.chefbook.ui.utils.compose.modifiers.clickable.composite.hoverScaleIndication
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import io.chefbook.ui.design.components.buttons.internal.BaseButton
import io.chefbook.ui.design.theme.shapes.SmoothCornerShape16

data class StandardButtonColors(
  val enabled: ButtonColors,
  val disabled: ButtonColors,
)

@Composable
fun StandardButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  shape: Shape = SmoothCornerShape16,
  colors: StandardButtonColors = standardButtonColors(),
  border: BorderStroke? = null,
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  interactionSource: MutableInteractionSource? = null,
  indication: Indication? = hoverScaleIndication(),
  debounceInterval: Long? = ShortDebounceInterval,
  horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
  content: @Composable RowScope.(InteractionSource) -> Unit,
) {
  val transition = updateTransition(enabled, label = "enabled")

  val containerColor by transition.animateColor(label = "containerColor") { enabled ->
    if (enabled) colors.enabled.containerColor else colors.disabled.containerColor
  }
  val contentColor by transition.animateColor(label = "contentColor") { enabled ->
    if (enabled) colors.enabled.contentColor else colors.disabled.contentColor
  }

  BaseButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    shape = shape,
    colors = ButtonColors(
      containerColor = containerColor,
      contentColor = contentColor,
    ),
    border = border,
    contentPadding = contentPadding,
    interactionSource = interactionSource,
    indication = indication,
    debounceInterval = debounceInterval,
    horizontalArrangement = horizontalArrangement,
    content = content,
  )
}

@Composable
fun standardButtonColors(
  containerColor: Color = LocalTheme.colors.backgroundSecondary,
  contentColor: Color = LocalTheme.colors.foregroundPrimary,
  disabledContainerColor: Color = LocalTheme.colors.backgroundSecondary,
  disabledContentColor: Color = LocalTheme.colors.foregroundSecondary,
): StandardButtonColors = StandardButtonColors(
  enabled = ButtonColors(
    containerColor = containerColor,
    contentColor = contentColor,
  ),
  disabled = ButtonColors(
    containerColor = disabledContainerColor,
    contentColor = disabledContentColor,
  ),
)
