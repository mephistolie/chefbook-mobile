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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.chefbook.core.commonGeneralEula
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.components.texts.HyperlinkText
import io.chefbook.features.auth.Res
import io.chefbook.features.auth.commonAuthScreenAgreement
import io.chefbook.features.auth.ui.blocks.AnimatedAuthForm
import io.chefbook.features.auth.ui.blocks.PasswordResetConfirmationForm
import io.chefbook.features.auth.ui.blocks.PasswordResetForm
import io.chefbook.features.auth.ui.blocks.ProfileActivationForm
import io.chefbook.features.auth.ui.blocks.ProfileRestorationForm
import io.chefbook.features.auth.ui.blocks.ProfilesListForm
import io.chefbook.features.auth.ui.blocks.SignInForm
import io.chefbook.features.auth.ui.blocks.SignInPasswordForm
import io.chefbook.features.auth.ui.blocks.SignUpForm
import io.chefbook.features.auth.ui.blocks.SignUpPasswordForm
import io.chefbook.features.auth.ui.components.ChefBookLogo
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import org.jetbrains.compose.resources.stringResource
import io.chefbook.core.Res as CoreR

@Composable
fun AuthScreenContent(
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
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .align(Alignment.TopCenter)
        .animateContentSize()
        .scrollable(orientation = Orientation.Vertical, state = rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Spacer(modifier = Modifier.height(128.dp))
      ChefBookLogo()
      Spacer(modifier = Modifier.height(20.dp))
      Box {
        AnimatedAuthForm<AuthScreenState.Loading>(state) {
//          CircularProgressIndicator(
//            color = colors.tintPrimary,
//            modifier = Modifier
//              .fillMaxWidth()
//              .wrapContentWidth()
//              .padding(24.dp, 24.dp, 24.dp)
//              .size(ComponentHeight48),
//            strokeCap = StrokeCap.Round,
//          )
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
        AnimatedAuthForm<AuthScreenState.SignInLogin>(state) {
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
        AnimatedAuthForm<AuthScreenState.ProfileList>(state) {
          ProfilesListForm(state = it, onIntent = onIntent)
        }
      }
    }

    when (state) {
      AuthScreenState.Loading, is AuthScreenState.ProfileRestoration -> Unit
      is AuthScreenState.PasswordReset,
      is AuthScreenState.PasswordResetConfirmation,
      is AuthScreenState.ProfileActivation,
      is AuthScreenState.SignInLogin,
      is AuthScreenState.SignInPassword,
      is AuthScreenState.SignUp,
      is AuthScreenState.SignUpPassword,
      is AuthScreenState.ProfileList -> HyperlinkText(
        text = stringResource(Res.string.commonAuthScreenAgreement),
        hyperlinks = listOf(stringResource(CoreR.string.commonGeneralEula) to "https://chefbook.io/eula"),
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
