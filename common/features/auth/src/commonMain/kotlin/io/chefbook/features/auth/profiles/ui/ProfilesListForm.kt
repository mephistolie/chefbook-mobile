package io.chefbook.features.auth.profiles.ui

import androidx.compose.runtime.Composable
import io.chefbook.ui.utils.compose.composables.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import io.chefbook.features.auth.profiles.component.ProfilesListComponent
import io.chefbook.features.auth.profiles.mvi.ProfilesListSideEffect

@Composable
fun ProfilesListForm(
  component: ProfilesListComponent,
  modifier: Modifier = Modifier,
) {
  LaunchedEffect {
    component.store.sideEffectsFlow.collect { sideEffect ->
      when (sideEffect) {
        is ProfilesListSideEffect.ProfileSelected -> component.onProfileSelected(sideEffect.profileId)
        ProfilesListSideEffect.AnotherProfileButtonClicked -> component.onAnotherProfileButtonClicked()
      }
    }
  }

  val state = component.store.stateFlow.collectAsState()
  ProfilesListFormContent(
    state = state.value,
    onIntent = component.store::handle,
  )
}
