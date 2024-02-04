package io.chefbook.features.auth.ui.blocks

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
import io.chefbook.features.auth.R
import io.chefbook.features.auth.ui.components.LoginInputField
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import io.chefbook.core.android.R as coreR

@Composable
internal fun SignUpForm(
  state: AuthScreenState.SignUp,
  onIntent: (AuthScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val focusRequester = remember { FocusRequester() }

  LoginInputField(
    value = state.email,
    onValueChange = { text -> onIntent(AuthScreenIntent.SetLogin(text)) },
    modifier = Modifier.focusRequester(focusRequester),
    imeAction = ImeAction.Done,
  )
  Spacer(Modifier.height(20.dp))
  DynamicButton(
    text = stringResource(id = coreR.string.common_general_next),
    onClick = { onIntent(AuthScreenIntent.OpenSignUpPasswordForm) },
    isEnabled = state.isAuthButtonEnabled,
    isSelected = state.isAuthButtonEnabled,
    textStyle = typography.headline1,
    modifier = Modifier
      .fillMaxWidth()
      .height(58.dp),
  )
  Spacer(Modifier.height(12.dp))
  Text(
    text = stringResource(R.string.common_auth_screen_already_member),
    style = typography.headline2,
    textAlign = TextAlign.Center,
    color = colors.tintPrimary,
    modifier = Modifier
      .fillMaxWidth()
      .simpleClickable { onIntent(AuthScreenIntent.OpenSignInForm) }
      .padding(vertical = 12.dp)
  )

  LaunchedEffect(Unit) {
    if (state.email.isNotEmpty()) focusRequester.requestFocus()
  }
}
