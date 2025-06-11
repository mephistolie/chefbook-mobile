package io.chefbook.features.auth.ui.blocks

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.theme.dimens.ComponentHeight56
import io.chefbook.features.auth.Res
import io.chefbook.features.auth.commonAuthScreenRepeatPassword
import io.chefbook.features.auth.commonAuthScreenSignUp
import io.chefbook.features.auth.ui.components.PasswordInputField
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import org.jetbrains.compose.resources.stringResource

@Composable
fun SignUpPasswordForm(
  state: AuthScreenState.SignUpPassword,
  onIntent: (AuthScreenIntent) -> Unit,
) {
  val typography = LocalTheme.typography

  val focusRequester = remember { FocusRequester() }

  AuthFormToolbar(
    login = state.email,
    onBackClick = { onIntent(AuthScreenIntent.OpenSignInForm) },
  )
  Spacer(Modifier.height(20.dp))
  PasswordInputField(
    value = state.password,
    onValueChange = { text -> onIntent(AuthScreenIntent.SetPassword(text)) },
    modifier = Modifier.focusRequester(focusRequester),
    imeAction = ImeAction.Next,
  )
  Spacer(Modifier.height(8.dp))
  PasswordInputField(
    value = state.passwordValidation,
    onValueChange = { text -> onIntent(AuthScreenIntent.SetPasswordValidation(text)) },
    hint = stringResource(Res.string.commonAuthScreenRepeatPassword),
    imeAction = ImeAction.Done,
  )
  Spacer(Modifier.height(20.dp))
  DynamicButton(
    text = stringResource(Res.string.commonAuthScreenSignUp),
    onClick = { onIntent(AuthScreenIntent.SignUp) },
    enabled = state.isAuthButtonEnabled,
    selected = state.isAuthButtonEnabled,
    textStyle = typography.headline1,
    modifier = Modifier
      .fillMaxWidth()
      .height(ComponentHeight56),
  )

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }
}
