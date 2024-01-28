package io.chefbook.design.components.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.chefbook.design.R
import io.chefbook.design.theme.dimens.MediumIconSize

@Composable
fun BottomSheetCloseButton(
  modifier: Modifier = Modifier,
  horizontalPadding: Dp = 16.dp,
  verticalPadding: Dp = horizontalPadding,
  iconSize: Dp = MediumIconSize,
  onClick: () -> Unit,
) {
  CircleIconButton(
    iconId = R.drawable.ic_cross,
    onClick = onClick,
    modifier = modifier
      .padding(
        horizontal = horizontalPadding,
        vertical = verticalPadding,
      )
      .size(iconSize),
    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black.copy(alpha = 0.25F)),
    tint = Color.White,
  )
}
