package io.chefbook.features.auth.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import io.chefbook.design.components.textfields.OutlinedTextField
import io.chefbook.core.android.R as coreR

@Composable
internal fun LoginInputField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  hint: String = stringResource(coreR.string.common_general_email),
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
      autoCorrect = false,
      keyboardType = KeyboardType.Email,
      imeAction = imeAction,
    ),
    keyboardActions = KeyboardActions(
      onDone = { keyboardController?.hide() }
    ),
    confidential = false
  )
}

@Composable
internal fun PasswordInputField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  readOnly: Boolean = false,
  hint: String = stringResource(coreR.string.common_general_password),
  imeAction: ImeAction = ImeAction.Done,
) {
  val keyboardController = LocalSoftwareKeyboardController.current

  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier.fillMaxWidth(),
    readOnly = readOnly,
    hint = hint,
    keyboardOptions = KeyboardOptions(
      autoCorrect = false,
      keyboardType = KeyboardType.Password,
      imeAction = imeAction,
    ),
    keyboardActions = KeyboardActions(
      onDone = { keyboardController?.hide() }
    ),
    confidential = true
  )
}
