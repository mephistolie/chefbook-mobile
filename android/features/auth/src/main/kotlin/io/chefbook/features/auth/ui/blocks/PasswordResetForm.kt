package io.chefbook.features.auth.ui.blocks

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.features.auth.R
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState

@Composable
internal fun PasswordResetForm(
  state: AuthScreenState.PasswordReset,
  onIntent: (AuthScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  AuthFormToolbar(
    login = state.login,
    onBackClick = { onIntent(AuthScreenIntent.OpenSignInForm) },
  )
  Spacer(Modifier.height(32.dp))
  Text(
    text = stringResource(R.string.common_auth_screen_password_reset_mail),
    textAlign = TextAlign.Center,
    style = typography.body2,
    color = colors.foregroundSecondary,
  )
}
