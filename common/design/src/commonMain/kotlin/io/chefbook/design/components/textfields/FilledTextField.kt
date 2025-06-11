package io.chefbook.design.components.textfields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.contentPaddingWithoutLabel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.components.textfields.internal.SingleLine
import io.chefbook.design.components.textfields.internal.StandardTextField
import io.chefbook.design.icons.ChefBookIcons
import io.chefbook.design.icons.Mail
import io.chefbook.design.theme.ChefBookTheme
import io.chefbook.design.theme.shapes.SmoothCornerShape12
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FilledTextField(
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
  visualTransformation: VisualTransformation = VisualTransformation.None,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions(),
  maxLines: Int = SingleLine,
  minLines: Int = SingleLine,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  contentPadding: PaddingValues = contentPaddingWithoutLabel(),
) {
  val colors = LocalTheme.colors

  StandardTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
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
    visualTransformation = visualTransformation,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    maxLines = maxLines,
    minLines = minLines,
    interactionSource = interactionSource,
    shape = SmoothCornerShape12,
    colors = TextFieldDefaults.colors(
      focusedTextColor = colors.foregroundPrimary,
      cursorColor = colors.tintPrimary,
      focusedContainerColor = colors.backgroundTertiary,
    ),
    contentPadding = contentPadding,
  )
}

@Composable
@Preview()
fun PreviewLightFilledTextField() {
  ThemedFilledTextField(false)
}

@Composable
@Preview()
fun PreviewDarkFilledTextField() {
  ThemedFilledTextField(true)
}

@Composable
private fun ThemedFilledTextField(
  isDarkTheme: Boolean
) {
  ChefBookTheme(darkTheme = isDarkTheme) {
    Surface(
      color = LocalTheme.colors.backgroundPrimary
    ) {
      FilledTextField(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        hint = "Email",
        leadingIcon = ChefBookIcons.Mail,
      )
    }
  }
}
