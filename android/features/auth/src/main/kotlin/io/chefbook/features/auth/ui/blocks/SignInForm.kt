package io.chefbook.features.auth.ui.blocks

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.components.dividers.Divider
import io.chefbook.features.auth.R
import io.chefbook.features.auth.ui.components.LoginInputField
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState

@Composable
internal fun SignInForm(
  state: AuthScreenState.SignIn,
  onIntent: (AuthScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val context = LocalContext.current

  val focusRequester = remember { FocusRequester() }

  LoginInputField(
    value = state.login,
    onValueChange = { text -> onIntent(AuthScreenIntent.SetLogin(text)) },
    modifier = Modifier.focusRequester(focusRequester),
    hint = stringResource(R.string.common_auth_screen_nickname_or_email),
    imeAction = ImeAction.Done,
  )
  Spacer(Modifier.height(20.dp))
  DynamicButton(
    text = stringResource(id = R.string.common_auth_screen_sign_in),
    onClick = { onIntent(AuthScreenIntent.OpenSignInPasswordForm) },
    isEnabled = state.isAuthButtonEnabled,
    isSelected = state.isAuthButtonEnabled,
    textStyle = typography.headline1,
    modifier = Modifier
      .fillMaxWidth()
      .height(58.dp),
  )
  Spacer(Modifier.height(8.dp))
  DynamicButton(
    text = stringResource(id = R.string.common_auth_screen_sign_up),
    onClick = { onIntent(AuthScreenIntent.OpenSignUpForm) },
    textStyle = typography.headline1,
    isSelected = true,
    selectedForeground = colors.foregroundPrimary,
    selectedBackground = colors.backgroundSecondary,
    modifier = Modifier
      .fillMaxWidth()
      .height(58.dp),
  )
  Spacer(Modifier.height(20.dp))
  Divider(
    color = colors.backgroundSecondary,
    modifier = Modifier
      .fillMaxWidth()
      .height(1.dp)
      .padding(96.dp, 0.dp)
  )
  Spacer(Modifier.height(15.dp))
  SignInOptionsBlock(
    onSignInGoogleClick = { onIntent(AuthScreenIntent.SignInGoogleClicked(context = context)) }
  )

  LaunchedEffect(Unit) {
    if (state.login.isNotEmpty()) focusRequester.requestFocus()
  }
}
