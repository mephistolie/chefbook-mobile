package io.chefbook.features.profile.editing.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.features.profile.editing.ui.mvi.ProfileEditingScreenEffect
import io.chefbook.features.profile.editing.ui.navigation.ProfileEditingScreenNavigator
import io.chefbook.ui.common.dialogs.LoadingDialog
import org.koin.androidx.compose.koinViewModel

@Destination(route = "profile/edit")
@Composable
fun ProfileEditingScreen(
  navigator: ProfileEditingScreenNavigator,
) {
  val viewModel = koinViewModel<ProfileEditingScreenViewModel>()
  val state = viewModel.state.collectAsStateWithLifecycle()

  ProfileEditingScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )
  if (state.value.isLoading) {
    LoadingDialog()
    BackHandler {}
  }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is ProfileEditingScreenEffect.ProfileDeletionScreenOpened -> navigator.openProfileDeletionScreen()
        is ProfileEditingScreenEffect.Closed -> navigator.navigateUp()
      }
    }
  }
}
