package io.chefbook.design.components.buttons

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.constants.ShortDebounceInterval
import io.chefbook.core.android.compose.providers.theme.LocalTheme

@Composable
fun SelectableButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  cornerRadius: Dp = 16.dp,
  selectedBackground: Color = LocalTheme.colors.tintPrimary,
  unselectedBackground: Color = LocalTheme.colors.backgroundSecondary,
  isSelected: Boolean = false,
  isEnabled: Boolean = true,
  disableScaling: Boolean = false,
  debounceInterval: Long? = ShortDebounceInterval,
  content: @Composable BoxScope.(Transition<Boolean>) -> Unit,
) {
  val transition = updateTransition(isSelected, label = "isChecked")

  val background by transition.animateColor(label = "background") { selected ->
    if (selected) selectedBackground else unselectedBackground
  }

  StandardButton(
    onClick = onClick,
    modifier = modifier,
    cornerRadius = cornerRadius,
    background = background,
    isEnabled = isEnabled,
    disableScaling = disableScaling,
    debounceInterval = debounceInterval,
  ) {
    content(transition)
  }
}
