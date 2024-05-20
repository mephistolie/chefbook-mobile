package io.chefbook.design.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.scalingClickable
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.constants.ShortDebounceInterval
import io.chefbook.core.android.compose.providers.theme.LocalTheme

@Composable
fun StandardButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  cornerRadius: Dp = 16.dp,
  background: Color = LocalTheme.colors.backgroundSecondary,
  isEnabled: Boolean = true,
  disableScaling: Boolean = false,
  debounceInterval: Long? = ShortDebounceInterval,
  content: @Composable BoxScope.() -> Unit,
) {
  val pressed = remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .run {
        when {
          isEnabled && disableScaling -> simpleClickable(
            debounceInterval = debounceInterval,
            onClick = onClick,
          )

          isEnabled -> scalingClickable(
            pressed = pressed,
            scaleFactor = 0.95F,
            onClick = onClick,
            debounceInterval = debounceInterval,
          )

          else -> this
        }
      }
      .clippedBackground(background, RoundedCornerShape(cornerRadius)),
    contentAlignment = Alignment.Center
  ) {
    content()
    Shading(pressed.value)
  }
}
