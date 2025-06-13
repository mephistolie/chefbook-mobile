package io.chefbook.features.auth.ui.blocks

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import io.chefbook.ui.design.components.buttons.selectableButtonColors
import io.chefbook.ui.design.components.dividers.Divider
import io.chefbook.ui.design.icons.ChefBookIcons
import io.chefbook.ui.design.icons.Users
import io.chefbook.ui.design.theme.dimens.ComponentHeight56
import io.chefbook.features.auth.Res
import io.chefbook.features.auth.commonAuthScreenNicknameOrEmail
import io.chefbook.features.auth.commonAuthScreenSignIn
import io.chefbook.features.auth.commonAuthScreenSignUp
import io.chefbook.features.auth.ui.components.LoginInputField
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import org.jetbrains.compose.resources.stringResource

@Composable
fun SignInForm(
  state: AuthScreenState.SignInLogin,
  onIntent: (AuthScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

//  val context = LocalContext.current

  val focusRequester = remember { FocusRequester() }

  Row {
    if (state.isProfileListButtonVisible) {
      DynamicButton(
        onClick = { onIntent(AuthScreenIntent.OpenProfileListForm) },
        leadIcon = ChefBookIcons.Users,
        selected = true,
        colors = selectableButtonColors(
          selectedContainerColor = colors.backgroundSecondary,
          selectedContentColor = colors.foregroundPrimary,
        ),
        modifier = Modifier.size(ComponentHeight56),
      )
      Spacer(modifier = Modifier.width(8.dp))
    }
    LoginInputField(
      value = state.login,
      onValueChange = { text -> onIntent(AuthScreenIntent.SetLogin(text)) },
      modifier = Modifier.focusRequester(focusRequester),
      hint = stringResource(Res.string.commonAuthScreenNicknameOrEmail),
      imeAction = ImeAction.Done,
    )
  }
  Spacer(Modifier.height(20.dp))
  DynamicButton(
    text = stringResource(Res.string.commonAuthScreenSignIn),
    onClick = { onIntent(AuthScreenIntent.OpenSignInPasswordForm) },
    enabled = state.isAuthButtonEnabled,
    selected = state.isAuthButtonEnabled,
    textStyle = typography.headline1,
    modifier = Modifier
      .fillMaxWidth()
      .height(ComponentHeight56),
  )
  Spacer(Modifier.height(8.dp))
  DynamicButton(
    text = stringResource(Res.string.commonAuthScreenSignUp),
    onClick = { onIntent(AuthScreenIntent.OpenSignUpForm) },
    textStyle = typography.headline1,
    selected = true,
    colors = selectableButtonColors(
      selectedContainerColor = colors.backgroundSecondary,
      selectedContentColor = colors.foregroundPrimary,
    ),
    modifier = Modifier
      .fillMaxWidth()
      .height(ComponentHeight56),
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
    onSignInGoogleClick = {},
//    onSignInGoogleClick = { onIntent(AuthScreenIntent.SignInGoogleClicked(context = context)) }
  )

  LaunchedEffect(Unit) {
    if (state.login.isNotEmpty()) focusRequester.requestFocus()
  }
}
