package io.chefbook.features.community.recipes.ui.screens.filter.components.blocks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.theme.shapes.RoundedCornerShape28Top
import io.chefbook.core.android.R as coreR

internal val buttonsBlockHeight = 80.dp

@Composable
internal fun ButtonsBlock(
  isResetButtonVisible: Boolean,
  onResetClick: () -> Unit,
  onConfirmClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors

  val density = LocalDensity.current

  val buttonWidth = remember { mutableStateOf(0.dp) }

  Row(
    modifier = modifier
      .background(colors.backgroundSecondary, RoundedCornerShape28Top)
      .padding(top = 1.dp)
      .background(colors.backgroundPrimary, RoundedCornerShape28Top)
      .navigationBarsPadding()
      .imePadding()
      .height(buttonsBlockHeight)
      .padding(12.dp)
      .onGloballyPositioned { coordinates ->
        buttonWidth.value = with(density) { coordinates.size.width.toDp() / 2 - 8.dp }
      },
  ) {
    AnimatedVisibility(
      visible = isResetButtonVisible,
    ) {
      DynamicButton(
        text = stringResource(coreR.string.common_general_reset),
        onClick = onResetClick,
        unselectedForeground = colors.foregroundPrimary,
        modifier = Modifier
          .width(buttonWidth.value)
          .fillMaxHeight(),
      )
    }
    DynamicButton(
      text = stringResource(coreR.string.common_general_confirm),
      onClick = onConfirmClick,
      isSelected = true,
      modifier = Modifier
        .weight(1F)
        .fillMaxSize(),
    )
  }
}
