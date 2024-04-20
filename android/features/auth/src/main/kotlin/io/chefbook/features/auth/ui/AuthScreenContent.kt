package io.chefbook.features.auth.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.text.HyperlinkText
import io.chefbook.design.theme.dimens.ComponentMediumHeight
import io.chefbook.features.auth.R
import io.chefbook.features.auth.ui.blocks.AnimatedAuthForm
import io.chefbook.features.auth.ui.blocks.PasswordResetConfirmationForm
import io.chefbook.features.auth.ui.blocks.PasswordResetForm
import io.chefbook.features.auth.ui.blocks.ProfileActivationForm
import io.chefbook.features.auth.ui.blocks.ProfileRestorationForm
import io.chefbook.features.auth.ui.blocks.SignInForm
import io.chefbook.features.auth.ui.blocks.SignInPasswordForm
import io.chefbook.features.auth.ui.blocks.SignUpForm
import io.chefbook.features.auth.ui.blocks.SignUpPasswordForm
import io.chefbook.features.auth.ui.components.ChefBookLogo
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import io.chefbook.core.android.R as coreR

@Composable
internal fun AuthScreenContent(
  state: AuthScreenState,
  onIntent: (AuthScreenIntent) -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(colors.backgroundPrimary)
      .systemBarsPadding(),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .animateContentSize()
        .scrollable(orientation = Orientation.Vertical, state = rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      ChefBookLogo()
      Spacer(modifier = Modifier.height(20.dp))
      Box {
        AnimatedAuthForm<AuthScreenState.Loading>(state) {
          CircularProgressIndicator(
            color = colors.tintPrimary,
            modifier = Modifier
              .fillMaxWidth()
              .wrapContentWidth()
              .padding(24.dp, 24.dp, 24.dp)
              .size(ComponentMediumHeight),
            strokeCap = StrokeCap.Round,
          )
        }
        AnimatedAuthForm<AuthScreenState.SignUp>(state) {
          SignUpForm(state = it, onIntent = onIntent)
        }
        AnimatedAuthForm<AuthScreenState.SignUpPassword>(state) {
          SignUpPasswordForm(state = it, onIntent = onIntent)
        }
        AnimatedAuthForm<AuthScreenState.ProfileActivation>(state) {
          ProfileActivationForm(state = it, onIntent = onIntent)
        }
        AnimatedAuthForm<AuthScreenState.SignIn>(state) {
          SignInForm(state = it, onIntent = onIntent)
        }
        AnimatedAuthForm<AuthScreenState.SignIn>(state) {
          SignInForm(state = it, onIntent = onIntent)
        }
        AnimatedAuthForm<AuthScreenState.SignInPassword>(state) {
          SignInPasswordForm(state = it, onIntent = onIntent)
        }
        AnimatedAuthForm<AuthScreenState.PasswordReset>(state) {
          PasswordResetForm(state = it, onIntent = onIntent)
        }
        AnimatedAuthForm<AuthScreenState.PasswordResetConfirmation>(state) {
          PasswordResetConfirmationForm(state = it, onIntent = onIntent)
        }
        AnimatedAuthForm<AuthScreenState.ProfileRestoration>(state) {
          ProfileRestorationForm(state = it, onIntent = onIntent)
        }
      }
    }

    if (state !is AuthScreenState.Loading && state !is AuthScreenState.ProfileRestoration) {
      HyperlinkText(
        text = stringResource(R.string.common_auth_screen_agreement),
        hyperlinks = listOf(stringResource(coreR.string.common_general_eula) to "https://chefbook.io/eula"),
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(
            start = 48.dp,
            end = 48.dp,
            bottom = 48.dp,
          ),
        style = typography.body2.copy(textAlign = TextAlign.Center),
      )
    }
  }
}
