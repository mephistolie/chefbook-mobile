package io.chefbook.design.components.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.scalingClickable
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.constants.ShortDebounceInterval
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.progress.CircularProgressIndicator
import io.chefbook.design.theme.dimens.DefaultIconSize
import io.chefbook.design.theme.shapes.RoundedCornerShape16

@Composable
fun LoadingButton(
  onClick: () -> Unit,
  isLoading: Boolean,
  modifier: Modifier = Modifier,
  cornerRadius: Dp = 16.dp,
  selectedBackground: Color = LocalTheme.colors.tintPrimary,
  unselectedBackground: Color = LocalTheme.colors.backgroundSecondary,
  selectedForeground: Color = if (LocalTheme.colors.isDark && selectedBackground.luminance() > 0.5F) Color.Black else Color.White,
  unselectedForeground: Color = LocalTheme.colors.foregroundSecondary,
  isSelected: Boolean = false,
  isEnabled: Boolean = true,
  disableScaling: Boolean = false,
  debounceInterval: Long? = ShortDebounceInterval,
  content: @Composable AnimatedVisibilityScope.(Transition<Boolean>) -> Unit,
) {
  SelectableButton(
    onClick = onClick,
    modifier = modifier,
    cornerRadius = cornerRadius,
    selectedBackground = selectedBackground,
    unselectedBackground = unselectedBackground,
    isSelected = isSelected,
    isEnabled = !isLoading && isEnabled,
    disableScaling = disableScaling,
    debounceInterval = debounceInterval,
  ) {transition ->
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      val foreground by transition.animateColor(label = "foreground") { selected ->
        if (selected) selectedForeground else unselectedForeground
      }

      AnimatedVisibility(
        visible = !isLoading,
        enter = slideInVertically(initialOffsetY = { -it }, animationSpec = tween()),
        exit = slideOutVertically(targetOffsetY = { -it }, animationSpec = tween()),
      ) {
        content(transition)
      }
      AnimatedVisibility(
        visible = isLoading,
        enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween()),
        exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween()),
      ) {
        CircularProgressIndicator(
          size = DefaultIconSize,
          color = foreground,
          strokeWidth = 2.dp,
        )
      }
    }
  }
}
