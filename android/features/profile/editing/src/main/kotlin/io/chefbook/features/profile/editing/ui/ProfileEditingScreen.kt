package io.chefbook.features.profile.editing.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.features.profile.editing.ui.mvi.ProfileEditingScreenEffect
import io.chefbook.navigation.navigators.BaseNavigator
import io.chefbook.ui.common.dialogs.LoadingDialog
import org.koin.androidx.compose.getViewModel

@Destination(route = "profile/edit")
@Composable
fun ProfileEditingScreen(
  navigator: BaseNavigator,
) {
  val viewModel: IProfileEditingScreenViewModel = getViewModel<ProfileEditingScreenViewModel>()
  val state = viewModel.state.collectAsState()

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
        is ProfileEditingScreenEffect.Closed -> navigator.navigateUp()
      }
    }
  }
}
