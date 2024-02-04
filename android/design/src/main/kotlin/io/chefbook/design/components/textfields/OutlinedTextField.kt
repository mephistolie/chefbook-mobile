package io.chefbook.design.components.textfields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.R
import io.chefbook.design.theme.ChefBookTheme
import io.chefbook.design.theme.colors.Red
import io.chefbook.design.theme.shapes.RoundedCornerShape16

@Composable
fun OutlinedTextField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  hint: String = "",
  readOnly: Boolean = false,
  icon: ImageVector? = null,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions(),
  isError: Boolean = false,
  minLines: Int = 1,
  maxLines: Int = 1,
  confidential: Boolean = false,
  textOpacity: Float = 1F
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val visible = remember { mutableStateOf(false) }

  androidx.compose.material.OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    readOnly = readOnly,
    textStyle = typography.body1,
    placeholder = {
      Text(
        text = hint,
        modifier = Modifier.alpha(textOpacity),
        style = typography.body1,
        color = colors.foregroundSecondary,
      )
    },
    leadingIcon = icon?.let {
      {
        Icon(
          imageVector = icon,
          tint = colors.foregroundSecondary,
          contentDescription = hint,
        )
      }
    },
    trailingIcon = if (confidential) {
      {
        IconButton(onClick = { visible.value = !visible.value }) {
          Icon(
            imageVector = ImageVector.vectorResource(if (visible.value) R.drawable.ic_eye else R.drawable.ic_eye_closed),
            tint = colors.foregroundSecondary,
            contentDescription = null,
          )
        }
      }
    } else null,
    isError = isError,
    visualTransformation = if (!confidential || visible.value) VisualTransformation.None else PasswordVisualTransformation(),
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    singleLine = maxLines == 1,
    minLines = minLines,
    maxLines = maxLines,
    shape = RoundedCornerShape16,
    colors = TextFieldDefaults.outlinedTextFieldColors(
      textColor = colors.foregroundPrimary,
      cursorColor = colors.tintPrimary,
      focusedBorderColor = colors.tintPrimary,
      unfocusedBorderColor = colors.backgroundTertiary,
      disabledBorderColor = colors.foregroundSecondary,
      errorBorderColor = Red,
    ),
  )
}

@Composable
@Preview(showBackground = true)
private fun PreviewLightOutlinedInputFields() {
  ThemedInputFields(false)
}

@Composable
@Preview(showBackground = true)
private fun PreviewDarkOutlinedInputFields() {
  ThemedInputFields(true)
}

@Composable
private fun ThemedInputFields(
  isDarkTheme: Boolean
) {
  ChefBookTheme(darkTheme = isDarkTheme) {
    Surface(
      color = LocalTheme.colors.backgroundPrimary
    ) {
      Column {
        FilledTextField(
          value = "",
          onValueChange = {},
          modifier = Modifier.fillMaxWidth(),
          hint = "Email",
          icon = Icons.Rounded.Email,
          confidential = false
        )
        Spacer(Modifier.height(8.dp))
        FilledTextField(
          value = "",
          onValueChange = {},
          modifier = Modifier.fillMaxWidth(),
          hint = "Password",
          icon = Icons.Rounded.Lock,
          confidential = true
        )
      }
    }
  }
}
