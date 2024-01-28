package io.chefbook.features.auth.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.chefbook.features.auth.ui.mvi.AuthAction
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import io.chefbook.features.auth.R

@Composable
internal fun InputBlock(
  state: AuthScreenState,
  onIntent: (AuthScreenIntent) -> Unit,
) {
  val email = state.email
  val password = state.password
  val passwordValidation = state.passwordValidation

  EmailInputField(
    value = email,
    onValueChange = { text -> onIntent(AuthScreenIntent.SetEmail(text)) },
    readOnly = state.isLoading,
    imeAction = if (state.action != AuthAction.RESET_PASSWORD) ImeAction.Next else ImeAction.Done
  )
  AnimatedVisibility(visible = state.action != AuthAction.RESET_PASSWORD) {
    PasswordInputField(
      value = password,
      onValueChange = { text -> onIntent(AuthScreenIntent.SetPassword(text)) },
      modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp),
      readOnly = state.isLoading,
      imeAction = if (state.action == AuthAction.SIGN_UP) ImeAction.Next else ImeAction.Done
    )
  }
  AnimatedVisibility(visible = state.action == AuthAction.SIGN_UP) {
    Spacer(Modifier.height(8.dp))
    PasswordInputField(
      value = passwordValidation,
      onValueChange = { text -> onIntent(AuthScreenIntent.SetPasswordValidation(text)) },
      modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp),
      readOnly = state.isLoading,
      hint = stringResource(id = R.string.common_auth_screen_repeat_password)
    )
  }
}