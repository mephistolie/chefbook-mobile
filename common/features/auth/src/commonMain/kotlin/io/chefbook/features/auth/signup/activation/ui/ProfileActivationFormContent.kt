package io.chefbook.features.auth.signup.activation.ui

import androidx.compose.runtime.Composable
import io.chefbook.features.auth.signup.activation.mvi.ProfileActivationIntent
import io.chefbook.features.auth.signup.activation.mvi.ProfileActivationState
import io.chefbook.features.auth.signup.activation.ui.components.CodeForm
import io.chefbook.features.auth.ui.mvi.AuthScreenState

@Composable
internal inline fun ProfileActivationFormContent(
  state: ProfileActivationState,
  crossinline onIntent: (ProfileActivationIntent) -> Unit,
) {
  CodeForm(
    login = state.email,
    code = state.code,
    codeLength = AuthScreenState.ProfileActivation.CODE_LENGTH,
    onCodeSet = { code -> onIntent(ProfileActivationIntent.CodeEntered(code)) },
    onBackClick = { onIntent(ProfileActivationIntent.Back) }
  )
}
