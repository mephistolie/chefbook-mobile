package io.chefbook.features.auth.ui.blocks

import androidx.compose.runtime.Composable
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.features.auth.ui.mvi.AuthScreenState

@Composable
internal inline fun ProfileActivationForm(
  state: AuthScreenState.ProfileActivation,
  crossinline onIntent: (AuthScreenIntent) -> Unit,
) {
  CodeForm(
    login = state.email,
    code = state.code,
    codeLength = AuthScreenState.ProfileActivation.CODE_LENGTH,
    onCodeSet = { code -> onIntent(AuthScreenIntent.SetActivationCode(code)) },
    onBackClick = { onIntent(AuthScreenIntent.OpenSignUpForm) }
  )
}
