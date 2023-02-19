package com.mysty.chefbook.features.auth.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.features.auth.R
import com.mysty.chefbook.features.auth.ui.mvi.AuthAction
import com.mysty.chefbook.features.auth.ui.mvi.AuthScreenIntent
import com.mysty.chefbook.features.auth.ui.mvi.AuthScreenState

@Composable
internal fun ScenarioSwitcher(
  state: AuthScreenState,
  onIntent: (AuthScreenIntent) -> Unit,
) {
  val focusManager = LocalFocusManager.current

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(48.dp),
    verticalArrangement = Arrangement.Bottom,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    AnimatedVisibility(visible = state.action != AuthAction.RESET_PASSWORD) {
      Text(
        text = stringResource(id = R.string.common_auth_screen_forgot_password),
        modifier = Modifier.simpleClickable {
          focusManager.clearFocus(force = true)
          onIntent(AuthScreenIntent.OpenPasswordResetScreen)
        },
        style = typography.body2,
        color = colors.tintPrimary
      )
    }
    Spacer(Modifier.height(8.dp))
    Row {
      Text(
        text = if (state.action != AuthAction.SIGN_UP) stringResource(id = R.string.common_auth_screen_not_member) else stringResource(
          id = R.string.common_auth_screen_already_member
        ),
        style = LocalTheme.typography.body2,
        color = LocalTheme.colors.foregroundSecondary,
        modifier = Modifier.padding(end = 4.dp)
      )
      Text(
        text = if (state.action == AuthAction.SIGN_IN) stringResource(id = R.string.common_auth_screen_sign_up) else stringResource(
          id = R.string.common_auth_screen_sign_in
        ),
        modifier = Modifier.simpleClickable {
          focusManager.clearFocus(force = true)
          if (state.action == AuthAction.SIGN_IN)
            onIntent(AuthScreenIntent.OpenSignUpScreen)
          else
            onIntent(AuthScreenIntent.OpenSignInScreen)
        },
        style = LocalTheme.typography.body2,
        color = colors.tintPrimary,
      )
    }
  }
}