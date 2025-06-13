package io.chefbook.features.auth.ui.blocks

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.chefbook.ui.utils.compose.modifiers.clickable.simpleClickable
import io.chefbook.ui.utils.compose.providers.theme.LocalTheme
import io.chefbook.ui.design.components.buttons.DynamicButton
import io.chefbook.ui.design.theme.dimens.ComponentHeight56
import io.chefbook.features.auth.Res
import io.chefbook.features.auth.commonAuthScreenForgotPassword
import io.chefbook.features.auth.commonAuthScreenSignIn
import io.chefbook.features.auth.ui.components.PasswordInputField
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import org.jetbrains.compose.resources.stringResource

@Composable
fun SignInPasswordForm(
  state: AuthScreenState.SignInPassword,
  onIntent: (AuthScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val focusRequester = remember { FocusRequester() }

  AuthFormToolbar(
    login = state.login,
    onBackClick = { onIntent(AuthScreenIntent.OpenSignInForm) },
  )
  Spacer(Modifier.height(20.dp))
  PasswordInputField(
    value = state.password,
    onValueChange = { text -> onIntent(AuthScreenIntent.SetPassword(text)) },
    modifier = Modifier.focusRequester(focusRequester),
    imeAction = ImeAction.Next,
  )
  Spacer(Modifier.height(20.dp))
  DynamicButton(
    text = stringResource(Res.string.commonAuthScreenSignIn),
    onClick = { onIntent(AuthScreenIntent.SignIn) },
    selected = true,
    modifier = Modifier
      .fillMaxWidth()
      .height(ComponentHeight56),
  )
  Spacer(Modifier.height(12.dp))
  Text(
    text = stringResource(Res.string.commonAuthScreenForgotPassword),
    style = typography.headline2,
    textAlign = TextAlign.Center,
    color = colors.tintPrimary,
    modifier = Modifier
      .fillMaxWidth()
      .simpleClickable { onIntent(AuthScreenIntent.RequestPasswordReset) }
      .padding(vertical = 12.dp)
  )

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }
}
