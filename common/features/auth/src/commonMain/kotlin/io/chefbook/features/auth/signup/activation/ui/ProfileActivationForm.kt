package io.chefbook.features.auth.signup.activation.ui

import androidx.compose.runtime.Composable
import io.chefbook.ui.utils.compose.composables.LaunchedEffect
import androidx.compose.runtime.collectAsState
import io.chefbook.features.auth.signup.activation.component.ProfileActivationComponent
import io.chefbook.features.auth.signup.activation.mvi.ProfileActivationSideEffect

@Composable
fun ProfileActivationForm(
  component: ProfileActivationComponent,
) {
  LaunchedEffect {
    component.store.sideEffectsFlow.collect { sideEffect ->
      return@collect when (sideEffect) {
        ProfileActivationSideEffect.Back -> component.onBackButtonClicked()
        ProfileActivationSideEffect.ProfileActivated -> component.onProfileActivated()
        is ProfileActivationSideEffect.ToastShown -> {
          // TODO
        }
      }
    }
  }

  val state = component.store.stateFlow.collectAsState()
  ProfileActivationFormContent(
    state = state.value,
    onIntent = component.store::handle,
  )
}
