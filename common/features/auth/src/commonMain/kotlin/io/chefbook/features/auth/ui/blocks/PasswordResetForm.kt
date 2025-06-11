package io.chefbook.features.auth.ui.blocks

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.features.auth.Res
import io.chefbook.features.auth.commonAuthScreenPasswordResetMail
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState
import org.jetbrains.compose.resources.stringResource

@Composable
fun PasswordResetForm(
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
    text = stringResource(Res.string.commonAuthScreenPasswordResetMail),
    textAlign = TextAlign.Center,
    style = typography.body2,
    color = colors.foregroundSecondary,
  )
}
