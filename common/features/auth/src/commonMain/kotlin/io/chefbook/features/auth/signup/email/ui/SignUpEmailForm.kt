package io.chefbook.features.auth.signup.email.ui

import androidx.compose.runtime.Composable
import io.chefbook.ui.utils.compose.composables.LaunchedEffect
import androidx.compose.runtime.collectAsState
import io.chefbook.features.auth.signup.email.component.SignUpEmailComponent
import io.chefbook.features.auth.signup.email.mvi.SignUpEmailSideEffect

@Composable
fun SignUpEmailForm(
  component: SignUpEmailComponent,
) {
  LaunchedEffect {
    component.store.sideEffectsFlow.collect { sideEffect ->
      return@collect when (sideEffect) {
        SignUpEmailSideEffect.SignedInProfilesButtonClicked -> component.onProfileListButtonClicked()

        is SignUpEmailSideEffect.NextButtonClicked -> component.onNextButtonClicked(sideEffect.email)

        is SignUpEmailSideEffect.SignInButtonClicked -> component.onSignInButtonClicked(sideEffect.currentEmailInput)
      }
    }
  }

  val state = component.store.stateFlow.collectAsState()
  SignUpEmailFormContent(
    state = state.value,
    onIntent = component.store::handle,
  )
}
