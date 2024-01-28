package io.chefbook.ui.common.dialogs.twobuttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.theme.ChefBookTheme
import io.chefbook.design.theme.shapes.DialogShape
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R

@Composable
internal fun TwoButtonsDialogContent(
  onLeftClick: () -> Unit,
  onRightClick: () -> Unit,
  title: String = stringResource(coreR.string.common_general_are_you_sure),
  description: String? = null,
  leftButtonIcon: ImageVector? = ImageVector.vectorResource(R.drawable.ic_cross),
  leftButtonText: String? = null,
  isLeftButtonPrimary: Boolean = false,
  rightButtonIcon: ImageVector? = ImageVector.vectorResource(R.drawable.ic_check),
  rightButtonText: String? = null,
  isRightButtonPrimary: Boolean = true,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(12.dp)
      .background(color = colors.backgroundPrimary, shape = DialogShape)
      .padding(12.dp, 20.dp, 12.dp, 12.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = title,
      style = typography.h3,
      color = colors.foregroundPrimary,
      textAlign = TextAlign.Center,
    )
    description?.let {
      Text(
        text = description,
        style = typography.body1,
        color = colors.foregroundSecondary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 12.dp)
      )
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(top = 32.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      DynamicButton(
        leftIcon = leftButtonIcon,
        text = leftButtonText,
        isSelected = isLeftButtonPrimary,
        unselectedForeground = colors.foregroundPrimary,
        modifier = Modifier
          .weight(1F)
          .fillMaxWidth()
          .height(48.dp),
        useSimpleClickable = true,
        onClick = onLeftClick,
      )
      DynamicButton(
        leftIcon = rightButtonIcon,
        text = rightButtonText,
        isSelected = isRightButtonPrimary,
        unselectedForeground = colors.foregroundPrimary,
        modifier = Modifier
          .weight(1F)
          .fillMaxWidth()
          .height(48.dp),
        useSimpleClickable = true,
        onClick = onRightClick,
      )
    }
  }
}

@Composable
@Preview(showBackground = true)
private fun PreviewLightTwoButtonsDialog() {
  ThemedTwoButtonsDialog(false)
}

@Composable
@Preview(showBackground = true)
private fun PreviewDarkTwoButtonsDialog() {
  ThemedTwoButtonsDialog(true)
}

@Composable
private fun ThemedTwoButtonsDialog(
  isDarkTheme: Boolean
) {
  ChefBookTheme(darkTheme = isDarkTheme) {
    Surface(
      color = LocalTheme.colors.backgroundPrimary
    ) {
      TwoButtonsDialogContent(
        onLeftClick = {},
        onRightClick = {},
      )
    }
  }
}