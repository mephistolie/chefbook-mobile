package io.chefbook.ui.design.components.textfields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults.contentPaddingWithoutLabel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import io.chefbook.ui.utils.commonGeneralHide
import io.chefbook.ui.utils.commonGeneralShow
import io.chefbook.ui.utils.Res as CoreRes
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import io.chefbook.ui.design.Res
import io.chefbook.ui.design.commonDesignVisibilitySwitching
import io.chefbook.ui.design.components.textfields.internal.SingleLine
import io.chefbook.ui.design.icons.ChefBookIcons
import io.chefbook.ui.design.icons.Eye
import io.chefbook.ui.design.icons.EyeCrossed
import io.chefbook.ui.design.icons.LockClosed
import io.chefbook.ui.design.theme.ChefBookTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ConfidentialFilledTextField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  readOnly: Boolean = false,
  hint: String? = null,
  hintOpacity: Float = 1F,
  leadingIcon: ImageVector? = null,
  leadingIconContentDescription: () -> String? = { null },
  onLeadingIconClickLabel: () -> String? = { null },
  onLeadingIconClick: (() -> Unit)? = null,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions(),
  maxLines: Int = SingleLine,
  minLines: Int = SingleLine,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  contentPadding: PaddingValues = contentPaddingWithoutLabel(),
) {
  val visible = remember { mutableStateOf(false) }

  FilledTextField(
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
    visualTransformation = if (visible.value) {
      VisualTransformation.None
    } else {
      PasswordVisualTransformation()
    },
    trailingIcon = if (visible.value) ChefBookIcons.Eye else ChefBookIcons.EyeCrossed,
    trailingIconContentDescription = { stringResource(Res.string.commonDesignVisibilitySwitching) },
    onTrailingIconClickLabel = {
      stringResource(
        resource = if (visible.value) {
          CoreRes.string.commonGeneralHide
        } else {
          CoreRes.string.commonGeneralShow
        }
      )
    },
    onTrailingIconClick = { visible.value = !visible.value },
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    maxLines = maxLines,
    minLines = minLines,
    interactionSource = interactionSource,
    contentPadding = contentPadding,
  )
}

@Composable
@Preview()
fun PreviewLightFilledConfidentialTextField() {
  ThemedFilledConfidentialTextField(false)
}

@Composable
@Preview()
fun PreviewDarkFilledConfidentialTextField() {
  ThemedFilledConfidentialTextField(true)
}

@Composable
private fun ThemedFilledConfidentialTextField(
  isDarkTheme: Boolean
) {
  ChefBookTheme(darkTheme = isDarkTheme) {
    Surface(
      color = LocalTheme.colors.backgroundPrimary
    ) {
      FilledTextField(
        value = "12345678",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        hint = "Password",
        leadingIcon = ChefBookIcons.LockClosed,
      )
    }
  }
}
