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
import io.chefbook.core.commonGeneralNext
import io.chefbook.core.compose.modifiers.clickable.simpleClickable
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.theme.dimens.ComponentHeight56
import io.chefbook.features.auth.Res
import io.chefbook.features.auth.commonAuthScreenAlreadyMember
import io.chefbook.features.auth.ui.components.LoginInputField
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import org.jetbrains.compose.resources.stringResource
import io.chefbook.core.Res as CoreR

@Composable
fun SignUpForm(
  state: AuthScreenState.SignUp,
  onIntent: (AuthScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val focusRequester = remember { FocusRequester() }

  LaunchedEffect(Unit) {
    if (state.email.isNotEmpty()) focusRequester.requestFocus()
  }

  LoginInputField(
    value = state.email,
    onValueChange = { text -> onIntent(AuthScreenIntent.SetLogin(text)) },
    modifier = Modifier.focusRequester(focusRequester),
    imeAction = ImeAction.Done,
  )
  Spacer(Modifier.height(20.dp))
  DynamicButton(
    text = stringResource(CoreR.string.commonGeneralNext),
    onClick = { onIntent(AuthScreenIntent.OpenSignUpPasswordForm) },
    enabled = state.isAuthButtonEnabled,
    selected = state.isAuthButtonEnabled,
    textStyle = typography.headline1,
    modifier = Modifier
      .fillMaxWidth()
      .height(ComponentHeight56),
  )
  Spacer(Modifier.height(12.dp))
  Text(
    text = stringResource(Res.string.commonAuthScreenAlreadyMember),
    style = typography.headline2,
    textAlign = TextAlign.Center,
    color = colors.tintPrimary,
    modifier = Modifier
      .fillMaxWidth()
      .simpleClickable { onIntent(AuthScreenIntent.OpenSignInForm) }
      .padding(vertical = 12.dp)
  )
}
