package io.chefbook.features.auth.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import io.chefbook.core.commonGeneralEmail
import io.chefbook.core.commonGeneralPassword
import io.chefbook.design.components.textfields.OutlinedConfidentialTextField
import io.chefbook.design.components.textfields.OutlinedTextField
import org.jetbrains.compose.resources.stringResource
import io.chefbook.core.Res as CoreR

@Composable
internal fun LoginInputField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  hint: String = stringResource(CoreR.string.commonGeneralEmail),
  readOnly: Boolean = false,
  imeAction: ImeAction = ImeAction.Next,
) {
  val keyboardController = LocalSoftwareKeyboardController.current

  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier.fillMaxWidth(),
    readOnly = readOnly,
    hint = hint,
    keyboardOptions = KeyboardOptions(
      autoCorrectEnabled = false,
      keyboardType = KeyboardType.Email,
      imeAction = imeAction,
    ),
    keyboardActions = KeyboardActions(
      onDone = { keyboardController?.hide() }
    ),
  )
}

@Composable
internal fun PasswordInputField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  readOnly: Boolean = false,
  hint: String = stringResource(CoreR.string.commonGeneralPassword),
  imeAction: ImeAction = ImeAction.Done,
) {
  val keyboardController = LocalSoftwareKeyboardController.current

  OutlinedConfidentialTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier.fillMaxWidth(),
    readOnly = readOnly,
    hint = hint,
    keyboardOptions = KeyboardOptions(
      autoCorrectEnabled = false,
      keyboardType = KeyboardType.Password,
      imeAction = imeAction,
    ),
    keyboardActions = KeyboardActions(
      onDone = { keyboardController?.hide() }
    ),
  )
}
