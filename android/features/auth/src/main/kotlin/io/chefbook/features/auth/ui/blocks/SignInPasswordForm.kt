package io.chefbook.features.auth.form.ui.blocks

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.features.auth.form.R
import io.chefbook.features.auth.form.ui.components.PasswordInputField
import io.chefbook.features.auth.form.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.form.ui.mvi.AuthScreenState

@Composable
internal fun SignInPasswordForm(
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
    text = stringResource(id = R.string.common_auth_screen_sign_in),
    onClick = { onIntent(AuthScreenIntent.SignIn) },
    isSelected = true,
    textStyle = typography.headline1,
    modifier = Modifier
      .fillMaxWidth()
      .height(58.dp),
  )
  Spacer(Modifier.height(12.dp))
  Text(
    text = stringResource(R.string.common_auth_screen_forgot_password),
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
