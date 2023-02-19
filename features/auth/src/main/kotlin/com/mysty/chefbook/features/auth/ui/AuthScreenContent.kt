package com.mysty.chefbook.features.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.theme.ChefBookTheme
import com.mysty.chefbook.features.auth.ui.components.AuthButton
import com.mysty.chefbook.features.auth.ui.components.ChefBookLogo
import com.mysty.chefbook.features.auth.ui.components.InputBlock
import com.mysty.chefbook.features.auth.ui.components.InvalidInputHint
import com.mysty.chefbook.features.auth.ui.components.ScenarioSwitcher
import com.mysty.chefbook.features.auth.ui.components.SignInOptionsBlock
import com.mysty.chefbook.features.auth.ui.mvi.AuthAction
import com.mysty.chefbook.features.auth.ui.mvi.AuthScreenIntent
import com.mysty.chefbook.features.auth.ui.mvi.AuthScreenState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun AuthScreenContent(
  state: AuthScreenState,
  onIntent: (AuthScreenIntent) -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 48.dp)
      .systemBarsPadding()
      .imePadding(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    ChefBookLogo(modifier = Modifier.padding(bottom = 16.dp),)
    InputBlock(
      state = state,
      onIntent = onIntent,
    )
    Spacer(Modifier.height(16.dp))
    InvalidInputHint(
      state = state,
      modifier = Modifier.padding(bottom = 16.dp),
    )
    if (state.isLoading) {
      CircularProgressIndicator(
        color = colors.tintPrimary,
        modifier = Modifier.size(36.dp)
      )
    } else {
      AuthButton(
        state = state,
        onClick = { onIntent(AuthScreenIntent.AuthButtonClicked) }
      )
    }
    SignInOptionsBlock(state = state)
  }

  ScenarioSwitcher(
    state = state,
    onIntent = onIntent,
  )
}

@Composable
@Preview(showBackground = true)
fun PreviewLightSignInScreen() {
  ThemedAuthScreen(AuthScreenState(action = AuthAction.SIGN_IN), isDarkTheme = false)
}

@Composable
@Preview(showBackground = true)
fun PreviewDarkSignUpScreen() {
  ThemedAuthScreen(
    AuthScreenState(action = AuthAction.SIGN_IN, isLoading = true),
    isDarkTheme = true
  )
}

@Composable
@Preview(showBackground = true)
fun PreviewDarkResetPasswordScreen() {
  ThemedAuthScreen(AuthScreenState(action = AuthAction.RESET_PASSWORD), false)
}

@Composable
private fun ThemedAuthScreen(
  viewState: AuthScreenState,
  isDarkTheme: Boolean
) {
  ChefBookTheme(darkTheme = isDarkTheme) {
    Surface(
      color = LocalTheme.colors.backgroundPrimary
    ) {
      AuthScreenContent(
        state = viewState,
        onIntent = {},
      )
    }
  }
}