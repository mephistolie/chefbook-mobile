package io.chefbook.features.auth.signup.password.ui

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
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import io.chefbook.ui.design.components.buttons.DynamicButton
import io.chefbook.ui.design.theme.dimens.ComponentHeight56
import io.chefbook.features.auth.Res
import io.chefbook.features.auth.commonAuthScreenRepeatPassword
import io.chefbook.features.auth.commonAuthScreenSignUp
import io.chefbook.features.auth.signup.password.mvi.SignUpPasswordIntent
import io.chefbook.features.auth.signup.password.mvi.SignUpPasswordState
import io.chefbook.features.auth.ui.blocks.AuthFormToolbar
import io.chefbook.features.auth.ui.components.PasswordInputField
import org.jetbrains.compose.resources.stringResource

@Composable
fun SignUpPasswordFormContent(
  state: SignUpPasswordState,
  onIntent: (SignUpPasswordIntent) -> Unit,
) {
  val typography = LocalTheme.typography

  val focusRequester = remember { FocusRequester() }

  AuthFormToolbar(
    login = state.email,
    onBackClick = { onIntent(SignUpPasswordIntent.Back) },
  )
  Spacer(Modifier.height(20.dp))
  PasswordInputField(
    value = state.password,
    onValueChange = { text -> onIntent(SignUpPasswordIntent.PasswordEntered(text)) },
    modifier = Modifier.focusRequester(focusRequester),
    imeAction = ImeAction.Next,
  )
  Spacer(Modifier.height(8.dp))
  PasswordInputField(
    value = state.passwordValidation,
    onValueChange = { text -> onIntent(SignUpPasswordIntent.PasswordValidationEntered(text)) },
    hint = stringResource(Res.string.commonAuthScreenRepeatPassword),
    imeAction = ImeAction.Done,
  )
  Spacer(Modifier.height(20.dp))
  DynamicButton(
    text = stringResource(Res.string.commonAuthScreenSignUp),
    onClick = { onIntent(SignUpPasswordIntent.SignUpButtonClicked) },
    enabled = state.isSignUpButtonEnabled,
    selected = state.isSignUpButtonEnabled,
    textStyle = typography.headline1,
    modifier = Modifier
      .fillMaxWidth()
      .height(ComponentHeight56),
  )

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }
}
