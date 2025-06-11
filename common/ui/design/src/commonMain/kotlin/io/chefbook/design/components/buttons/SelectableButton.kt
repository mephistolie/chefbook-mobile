package io.chefbook.design.components.buttons

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
import androidx.compose.ui.graphics.luminance
import io.chefbook.core.compose.constants.ShortDebounceInterval
import io.chefbook.core.compose.modifiers.clickable.composite.hoverScaleIndication
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.internal.BaseButton
import io.chefbook.design.theme.shapes.SmoothCornerShape16

private class SelectableButtonState(
  val enabled: Boolean,
  val selected: Boolean,
)

data class SelectableButtonColors(
  val selected: ButtonColors,
  val unselected: ButtonColors,
  val disabled: ButtonColors,
)

@Composable
fun SelectableButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  selected: Boolean = false,
  shape: Shape = SmoothCornerShape16,
  colors: SelectableButtonColors = selectableButtonColors(),
  border: BorderStroke? = null,
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  interactionSource: MutableInteractionSource? = null,
  indication: Indication? = hoverScaleIndication(),
  debounceInterval: Long? = ShortDebounceInterval,
  horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
  content: @Composable RowScope.(InteractionSource) -> Unit,
) {
  val state = SelectableButtonState(selected = selected, enabled = enabled)
  val transition = updateTransition(state, label = "state")

  val containerColor by transition.animateColor(label = "containerColor") { state ->
    when {
      state.enabled && state.selected -> colors.selected.containerColor
      state.enabled -> colors.unselected.containerColor
      else -> colors.disabled.containerColor
    }
  }
  val contentColor by transition.animateColor(label = "contentColor") { state ->
    when {
      state.enabled && state.selected -> colors.selected.contentColor
      state.enabled -> colors.unselected.contentColor
      else -> colors.disabled.contentColor
    }
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
fun selectableButtonColors(
  selectedContainerColor: Color = LocalTheme.colors.tintPrimary,
  selectedContentColor: Color = if (LocalTheme.colors.isDark && selectedContainerColor.luminance() > 0.5F) Color.Black else Color.White,
  unselectedContainerColor: Color = LocalTheme.colors.backgroundSecondary,
  unselectedContentColor: Color = LocalTheme.colors.foregroundPrimary,
  disabledContainerColor: Color = LocalTheme.colors.backgroundSecondary,
  disabledContentColor: Color = LocalTheme.colors.foregroundSecondary,
): SelectableButtonColors = SelectableButtonColors(
  selected = ButtonColors(
    containerColor = selectedContainerColor,
    contentColor = selectedContentColor,
  ),
  unselected = ButtonColors(
    containerColor = unselectedContainerColor,
    contentColor = unselectedContentColor,
  ),
  disabled = ButtonColors(
    containerColor = disabledContainerColor,
    contentColor = disabledContentColor,
  ),
)
