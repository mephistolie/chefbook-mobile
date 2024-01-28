package io.chefbook.design.components.counter

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.ui.textfields.TextField
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.R
import io.chefbook.design.theme.ChefBookTheme
import io.chefbook.design.theme.dimens.ButtonSmallHeight
import io.chefbook.design.theme.shapes.RoundedCornerShape12

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Counter(
  count: Int,
  onMinusClicked: () -> Unit,
  onPlusClicked: () -> Unit,
  onValueChange: (String) -> Unit = {},
  minCount: Int = 1,
  maxCount: Int = 99,
  isTextEditable: Boolean = false,
  isMultiplier: Boolean = false,
) {
  val keyboardController = LocalSoftwareKeyboardController.current

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val text = when {
    count == 0 -> ""
    isMultiplier -> "x$count"
    else -> "$count"
  }

  Row(
    modifier = Modifier
      .wrapContentWidth()
      .height(ButtonSmallHeight)
      .clippedBackground(colors.backgroundSecondary, RoundedCornerShape12)
  ) {
    CounterButton(
      iconId = R.drawable.ic_remove,
      isEnabled = count > minCount,
      onClick = onMinusClicked
    )
    CounterDecorator()
    Box(
      modifier = Modifier
        .width(48.dp)
        .fillMaxHeight()
        .background(colors.backgroundSecondary),
      contentAlignment = Alignment.Center,
    ) {
      TextField(
        value = text,
        onValueChange = onValueChange,
        modifier = Modifier.clip(RectangleShape),
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Number,
          imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
          onDone = { keyboardController?.hide() }
        ),
        readOnly = !isTextEditable,
        textStyle = typography.headline1.copy(
          color = colors.foregroundPrimary,
          textAlign = TextAlign.Center
        ),
        contentPadding = PaddingValues(0.dp)
      )
    }
    CounterDecorator()
    CounterButton(iconId = R.drawable.ic_add, isEnabled = count < maxCount, onClick = onPlusClicked)
  }
}

@Composable
private fun CounterButton(
  @DrawableRes iconId: Int,
  isEnabled: Boolean,
  onClick: () -> Unit,
) =
  IconButton(
    onClick = onClick,
    modifier = Modifier.size(ButtonSmallHeight),
    enabled = isEnabled,
  ) {
    Icon(
      imageVector = ImageVector.vectorResource(iconId),
      tint = if (isEnabled) LocalTheme.colors.foregroundPrimary else LocalTheme.colors.foregroundSecondary,
      contentDescription = null,
    )
  }

@Composable
private fun CounterDecorator() =
  Spacer(
    modifier = Modifier
      .width(2.dp)
      .fillMaxHeight()
      .background(LocalTheme.colors.backgroundTertiary)
  )

@Composable
@Preview(showBackground = true)
fun PreviewLightCounter() {
  ThemedCounter(false)
}

@Composable
@Preview(showBackground = true)
fun PreviewDarkCounter() {
  ThemedCounter(true)
}

@Composable
private fun ThemedCounter(
  isDarkTheme: Boolean
) {
  ChefBookTheme(darkTheme = isDarkTheme) {
    Surface(
      color = LocalTheme.colors.backgroundPrimary
    ) {
      Counter(
        count = 1,
        onMinusClicked = {},
        onPlusClicked = {},
      )
    }
  }
}
