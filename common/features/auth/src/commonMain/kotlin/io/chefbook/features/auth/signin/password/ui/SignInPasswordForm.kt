package io.chefbook.features.auth.signin.password.ui

import androidx.compose.runtime.Composable
import io.chefbook.ui.utils.compose.composables.LaunchedEffect
import androidx.compose.runtime.collectAsState
import io.chefbook.features.auth.signin.password.component.SignInPasswordComponent
import io.chefbook.features.auth.signin.password.mvi.SignInPasswordSideEffect

@Composable
fun SignInPasswordForm(
  component: SignInPasswordComponent,
) {
  LaunchedEffect {
    component.store.sideEffectsFlow.collect { sideEffect ->
      return@collect when (sideEffect) {
        SignInPasswordSideEffect.Back -> component.onBackButtonClicked()
        SignInPasswordSideEffect.ResetPasswordButtonClicked -> component.onResetPasswordClicked()
        is SignInPasswordSideEffect.ToastShown -> {
          // TODO
        }
      }
    }
  }

  val state = component.store.stateFlow.collectAsState()
  PasswordFormContent(
    state = state.value,
    onIntent = component.store::handle,
  )
}
