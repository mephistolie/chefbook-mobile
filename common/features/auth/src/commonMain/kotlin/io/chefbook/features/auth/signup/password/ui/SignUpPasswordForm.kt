package io.chefbook.features.auth.signup.password.ui

import androidx.compose.runtime.Composable
import io.chefbook.ui.utils.compose.composables.LaunchedEffect
import androidx.compose.runtime.collectAsState
import io.chefbook.features.auth.signup.password.component.SignUpPasswordComponent
import io.chefbook.features.auth.signup.password.mvi.SignUpPasswordSideEffect

@Composable
fun SignUpPasswordForm(
  component: SignUpPasswordComponent,
) {
  LaunchedEffect {
    component.store.sideEffectsFlow.collect { sideEffect ->
      return@collect when (sideEffect) {
        SignUpPasswordSideEffect.Back -> component.onBack()
        is SignUpPasswordSideEffect.ProfileExists -> component.onProfileExists()
        is SignUpPasswordSideEffect.ProfileBlocked -> component.onProfileBlocked()
        is SignUpPasswordSideEffect.ToastShown -> {
          // TODO
        }
      }
    }
  }

  val state = component.store.stateFlow.collectAsState()
  SignUpPasswordFormContent(
    state = state.value,
    onIntent = component.store::handle,
  )
}
