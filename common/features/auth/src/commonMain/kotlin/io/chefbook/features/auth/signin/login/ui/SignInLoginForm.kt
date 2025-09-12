package io.chefbook.features.auth.signin.login.ui

import androidx.compose.runtime.Composable
import io.chefbook.ui.utils.compose.composables.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import io.chefbook.features.auth.signin.login.component.SignInLoginComponent
import io.chefbook.features.auth.signin.login.mvi.SignInLoginSideEffect

@Composable
fun SignInLoginForm(
  component: SignInLoginComponent,
  modifier: Modifier = Modifier,
) {
  LaunchedEffect {
    component.store.sideEffectsFlow.collect { sideEffect ->
      return@collect when (sideEffect) {
        is SignInLoginSideEffect.SignInButtonClicked ->
          component.onSignInButtonClicked(sideEffect.login)

        SignInLoginSideEffect.SignedInProfilesButtonClicked ->
          component.onProfileListButtonClicked()

        is SignInLoginSideEffect.SignedUpProfilesButtonClicked ->
          component.onSignUpButtonClicked(sideEffect.login)
      }
    }
  }

  val state = component.store.stateFlow.collectAsState()
  SignInLoginFormContent(
    state = state.value,
    onIntent = component.store::handle,
    modifier = modifier,
  )
}
