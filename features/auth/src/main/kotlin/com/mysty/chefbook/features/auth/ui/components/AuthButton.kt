package com.mysty.chefbook.features.auth.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.features.auth.R
import com.mysty.chefbook.features.auth.ui.mvi.AuthAction
import com.mysty.chefbook.features.auth.ui.mvi.AuthScreenState
import com.mysty.chefbook.features.auth.utils.PasswordRating

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun AuthButton(
  state: AuthScreenState,
  onClick: () -> Unit,
) {
  val keyboardController = LocalSoftwareKeyboardController.current

  val typography = LocalTheme.typography

  val isEmailValid = state.isEmailValid
  val isPasswordValid = state.passwordRating == PasswordRating.VALID
  val isInputValid = when (state.action) {
    AuthAction.SIGN_IN -> isEmailValid && state.password.isNotEmpty()
    AuthAction.SIGN_UP -> isEmailValid && isPasswordValid
    AuthAction.RESET_PASSWORD -> isEmailValid
  }

  DynamicButton(
    text = when (state.action) {
      AuthAction.SIGN_IN -> stringResource(id = R.string.common_auth_screen_sign_in).uppercase()
      AuthAction.SIGN_UP -> stringResource(id = R.string.common_auth_screen_sign_up).uppercase()
      AuthAction.RESET_PASSWORD -> stringResource(id = R.string.common_auth_screen_reset_password).uppercase()
    },
    onClick = {
      keyboardController?.hide()
      onClick()
    },
    isEnabled = isInputValid,
    isSelected = isInputValid,
    textStyle = typography.body1,
    modifier = Modifier
      .fillMaxWidth()
      .height(48.dp),
  )
}