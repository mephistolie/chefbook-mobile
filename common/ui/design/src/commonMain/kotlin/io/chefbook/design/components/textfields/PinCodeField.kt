package io.chefbook.design.components.textfields

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.components.textfields.internal.TextField
import io.chefbook.design.theme.ChefBookTheme
import io.chefbook.design.theme.dimens.ComponentHeight48
import io.chefbook.design.theme.shapes.NoPaddings
import io.chefbook.design.theme.shapes.SmoothCornerShape8
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val PinCodePrefix = "pin_code_field"
private const val IsFocusedLabel = "${PinCodePrefix}_is_focused"
private const val BorderColorLabel = "${PinCodePrefix}_border_color"
private const val HasValueLabel = "${PinCodePrefix}_has_value"
private const val TextOffsetLabel = "${PinCodePrefix}_pin_code_field_text_offset"

@Composable
fun PinCodeField(
  value: Char?,
  onPinCodeNumAdd: (Int) -> Unit,
  onPinCodeNumRemove: () -> Unit,
  modifier: Modifier = Modifier,
  isFocused: Boolean = false,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val focusedTransition = updateTransition(isFocused, label = IsFocusedLabel)
  val borderColor by focusedTransition.animateColor(label = BorderColorLabel) { selected ->
    if (selected) colors.tintPrimary else colors.backgroundSecondary
  }

  val valueTransition = updateTransition(value, label = HasValueLabel)
  val textOffset by valueTransition.animateDp(label = TextOffsetLabel) { input ->
    if (input != null) (-2).dp else 48.dp
  }


  Box(
    modifier = modifier
      .width(32.dp)
      .height(ComponentHeight48)
      .clip(SmoothCornerShape8)
      .border(
        width = 2.dp,
        brush = SolidColor(borderColor),
        shape = SmoothCornerShape8,
      ),
    contentAlignment = Alignment.Center,
  ) {
    TextField(
      value = value?.toString() ?: "",
      onValueChange = { it.toIntOrNull()?.let(onPinCodeNumAdd) },
      contentPadding = NoPaddings,
      visualTransformation = PasswordVisualTransformation(),
      colors = TextFieldDefaults.colors(
        focusedTextColor = colors.foregroundPrimary,
        disabledTextColor = colors.foregroundPrimary,
        cursorColor = Color.Transparent,
        errorCursorColor = Color.Transparent,
      ),
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.None
      ),
      textStyle = typography.h1.copy(
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium,
      ),
      modifier = Modifier
        .onKeyEvent { event ->
          if (event.type == KeyEventType.KeyUp && event.key == Key.Backspace) {
            onPinCodeNumRemove()
            return@onKeyEvent true
          }
          false
        }
        .offset(y = textOffset)
    )
  }
}

@Composable
@Preview()
fun PreviewLightPinCodeField() {
  ThemedPinCodeField(
    isDarkTheme = false,
    isFocused = true,
  )
}

@Composable
@Preview()
fun PreviewDarkPinCodeField() {
  ThemedPinCodeField(
    isDarkTheme = true,
    isFocused = false,
  )
}

@Composable
private fun ThemedPinCodeField(
  isDarkTheme: Boolean,
  isFocused: Boolean,
) {
  ChefBookTheme(darkTheme = isDarkTheme) {
    Surface(
      color = LocalTheme.colors.backgroundPrimary
    ) {
      PinCodeField(
        value = '1',
        onPinCodeNumAdd = {},
        onPinCodeNumRemove = {},
        isFocused = isFocused,
      )
    }
  }
}
