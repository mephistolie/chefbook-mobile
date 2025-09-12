package io.chefbook.ui.design.components.textfields.internal

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.VisualTransformation
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme

@Composable
internal fun StandardTextField(
  value: String,
  onValueChange: (String) -> Unit,
  selection: MutableState<TextRange> = remember { mutableStateOf(TextRange.Zero) },
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  readOnly: Boolean = false,
  hint: String? = null,
  hintOpacity: Float = 1F,
  leadingIcon: ImageVector? = null,
  leadingIconContentDescription: @Composable () -> String? = { null },
  onLeadingIconClickLabel: @Composable () -> String? = { null },
  onLeadingIconClick: (() -> Unit)? = null,
  trailingIcon: ImageVector? = null,
  trailingIconContentDescription: @Composable () -> String? = { null },
  onTrailingIconClickLabel: @Composable () -> String? = { null },
  onTrailingIconClick: (() -> Unit)? = null,
  isError: Boolean = false,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions(),
  maxLines: Int = SingleLine,
  minLines: Int = SingleLine,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  shape: Shape = TextFieldDefaults.shape,
  colors: TextFieldColors = TextFieldDefaults.colors(),
  contentPadding: PaddingValues = TextFieldDefaults.contentPaddingWithoutLabel(),
  decorationBoxStyle: DecorationBoxStyle = DecorationBoxStyle.Default,
) {
  val typography = LocalTheme.typography

  TextField(
    value = value,
    onValueChange = onValueChange,
    selection = selection,
    enabled = enabled,
    modifier = modifier,
    readOnly = readOnly,
    textStyle = typography.body1,
    placeholder = hint?.let {
      {
        Text(
          text = hint,
          modifier = Modifier.alpha(hintOpacity),
          style = typography.body1,
          color = LocalTheme.colors.foregroundSecondary,
        )
      }
    },
    lead = leadingIcon?.let {
      {
        TextFieldIcon(
          imageVector = leadingIcon,
          contentDescription = leadingIconContentDescription,
          onClickLabel = onLeadingIconClickLabel,
          onClick = onLeadingIconClick,
        )
      }
    },
    trail = trailingIcon?.let {
      {
        TextFieldIcon(
          imageVector = trailingIcon,
          contentDescription = trailingIconContentDescription,
          onClickLabel = onTrailingIconClickLabel,
          onClick = onTrailingIconClick,
        )
      }
    },
    isError = isError,
    visualTransformation = visualTransformation,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    maxLines = maxLines,
    minLines = minLines,
    interactionSource = interactionSource,
    shape = shape,
    colors = colors,
    contentPadding = contentPadding,
    decorationBoxStyle = decorationBoxStyle,
  )
}
