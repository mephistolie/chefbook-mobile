package io.chefbook.ui.design.components.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.chefbook.ui.utils.compose.constants.ShortDebounceInterval
import io.chefbook.ui.utils.compose.modifiers.clickable.composite.hoverScaleIndication
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import io.chefbook.ui.design.theme.shapes.SmoothCornerShape16

@Composable
fun DynamicButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  selected: Boolean = false,
  shape: Shape = SmoothCornerShape16,
  colors: SelectableButtonColors = selectableButtonColors(),
  border: BorderStroke? = null,
  contentPadding: PaddingValues = PaddingValues(horizontal = 10.dp),
  interactionSource: MutableInteractionSource? = null,
  indication: Indication? = hoverScaleIndication(),
  debounceInterval: Long? = ShortDebounceInterval,
  iconsModifier: Modifier = Modifier,
  text: String? = null,
  textStyle: TextStyle = LocalTheme.typography.headline1,
  leadIcon: ImageVector? = null,
  leadIconModifier: Modifier = iconsModifier,
  trailIcon: ImageVector? = null,
  trailIconModifier: Modifier = iconsModifier,
) {
  DynamicButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    selected = selected,
    shape = shape,
    colors = colors,
    border = border,
    contentPadding = contentPadding,
    interactionSource = interactionSource,
    indication = indication,
    debounceInterval = debounceInterval,
    lead = @Composable {
      ButtonIcon(
        imageVector = leadIcon,
        modifier = leadIconModifier,
      )
    },
    leadVisible = leadIcon != null,
    trail = @Composable {
      ButtonIcon(
        imageVector = trailIcon,
        modifier = trailIconModifier,
      )
    },
    trailVisible = trailIcon != null,
    center = @Composable {
      ButtonText(
        text = text,
        textStyle = textStyle,
      )
    },
    centerVisible = text != null,
  )
}


@Composable
fun DynamicButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  selected: Boolean = false,
  shape: Shape = SmoothCornerShape16,
  colors: SelectableButtonColors = selectableButtonColors(),
  border: BorderStroke? = null,
  contentPadding: PaddingValues = PaddingValues(horizontal = 10.dp),
  interactionSource: MutableInteractionSource? = null,
  indication: Indication? = hoverScaleIndication(),
  debounceInterval: Long? = ShortDebounceInterval,
  lead: @Composable (InteractionSource) -> Unit = {},
  leadVisible: Boolean = true,
  center: @Composable (InteractionSource) -> Unit = {},
  centerVisible: Boolean = true,
  trail: @Composable (InteractionSource) -> Unit = {},
  trailVisible: Boolean = true,
) {
  SelectableButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    selected = selected,
    shape = shape,
    colors = colors,
    border = border,
    contentPadding = contentPadding,
    interactionSource = interactionSource,
    indication = indication,
    debounceInterval = debounceInterval,
    horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterHorizontally),
  ) { transition ->
      AnimatedVisibility(leadVisible) {
        lead(transition)
      }
      AnimatedVisibility(centerVisible) {
        center(transition)
      }
      AnimatedVisibility(trailVisible) {
        trail(transition)
    }
  }
}
