package io.chefbook.ui.design.components.textfields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.contentPaddingWithoutLabel
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import io.chefbook.ui.design.components.textfields.internal.StandardTextField
import io.chefbook.ui.design.icons.ChefBookIcons
import io.chefbook.ui.design.icons.Mail
import io.chefbook.ui.design.theme.ChefBookTheme
import io.chefbook.ui.design.theme.dimens.DividerHeight
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndicatorLineTextField(
  value: String,
  onValueChange: (String) -> Unit,
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
  maxLines: Int = Int.MAX_VALUE,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  contentPadding: PaddingValues = contentPaddingWithoutLabel(),
) {
  val colors = LocalTheme.colors

  val textFieldColors = TextFieldDefaults.colors(
    focusedTextColor = colors.foregroundPrimary,
    disabledTextColor = colors.foregroundPrimary,
    cursorColor = colors.tintPrimary,
    focusedLabelColor = colors.tintPrimary,
    focusedIndicatorColor = colors.tintPrimary,
    disabledIndicatorColor = colors.backgroundSecondary,
    unfocusedIndicatorColor = colors.backgroundSecondary,
    focusedContainerColor = Color.Transparent,
  )

  StandardTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier
      .indicatorLine(
        enabled = true,
        isError = isError,
        interactionSource = interactionSource,
        colors = textFieldColors,
        focusedIndicatorLineThickness = DividerHeight,
        unfocusedIndicatorLineThickness = DividerHeight,
      ),
    enabled = enabled,
    readOnly = readOnly,
    hint = hint,
    hintOpacity = hintOpacity,
    leadingIcon = leadingIcon,
    leadingIconContentDescription = leadingIconContentDescription,
    onLeadingIconClickLabel = onLeadingIconClickLabel,
    onLeadingIconClick = onLeadingIconClick,
    trailingIcon = trailingIcon,
    trailingIconContentDescription = trailingIconContentDescription,
    onTrailingIconClickLabel = onTrailingIconClickLabel,
    onTrailingIconClick = onTrailingIconClick,
    isError = isError,
    visualTransformation = visualTransformation,
    keyboardActions = keyboardActions,
    keyboardOptions = keyboardOptions,
    maxLines = maxLines,
    interactionSource = interactionSource,
    colors = textFieldColors,
    contentPadding = contentPadding,
  )
}

@Composable
@Preview()
fun PreviewLightIndicatorLineTextField() {
  ThemedIndicatorLineTextField(false)
}

@Composable
@Preview()
fun PreviewDarkIndicatorLineTextField() {
  ThemedIndicatorLineTextField(true)
}

@Composable
private fun ThemedIndicatorLineTextField(
  isDarkTheme: Boolean
) {
  ChefBookTheme(darkTheme = isDarkTheme) {
    Surface(
      color = LocalTheme.colors.backgroundPrimary
    ) {
      IndicatorLineTextField(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = ChefBookIcons.Mail,
      )
    }
  }
}
