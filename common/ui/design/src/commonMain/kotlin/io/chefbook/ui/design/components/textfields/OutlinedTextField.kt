package io.chefbook.ui.design.components.textfields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults.contentPaddingWithoutLabel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import io.chefbook.ui.design.components.textfields.internal.DecorationBoxStyle
import io.chefbook.ui.design.components.textfields.internal.SingleLine
import io.chefbook.ui.design.components.textfields.internal.StandardTextField
import io.chefbook.ui.design.icons.ChefBookIcons
import io.chefbook.ui.design.icons.Mail
import io.chefbook.ui.design.theme.ChefBookTheme
import io.chefbook.ui.design.theme.colors.Red
import io.chefbook.ui.design.theme.shapes.SmoothCornerShape20
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OutlinedTextField(
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
    shape = SmoothCornerShape20,
    colors = OutlinedTextFieldDefaults.colors(
      focusedTextColor = colors.foregroundPrimary,
      cursorColor = colors.tintPrimary,
      focusedBorderColor = colors.tintPrimary,
      unfocusedBorderColor = colors.backgroundTertiary,
      disabledBorderColor = colors.foregroundSecondary,
      errorBorderColor = Red,
    ),
    contentPadding = contentPadding,
    decorationBoxStyle = DecorationBoxStyle.Outlined,
  )
}


@Composable
@Preview()
fun PreviewLightOutlinedTextField() {
  ThemedOutlinedTextField(false)
}

@Composable
@Preview()
fun PreviewDarkOutlinedTextField() {
  ThemedOutlinedTextField(true)
}

@Composable
private fun ThemedOutlinedTextField(
  isDarkTheme: Boolean
) {
  ChefBookTheme(darkTheme = isDarkTheme) {
    Surface(
      color = LocalTheme.colors.backgroundPrimary
    ) {
      OutlinedTextField(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        hint = "Email",
        leadingIcon = ChefBookIcons.Mail,
      )
    }
  }
}
