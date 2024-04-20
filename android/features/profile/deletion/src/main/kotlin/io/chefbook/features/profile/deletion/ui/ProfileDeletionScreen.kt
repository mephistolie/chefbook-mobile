package io.chefbook.features.profile.deletion.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.core.android.showToast
import io.chefbook.features.profile.deletion.ui.mvi.ProfileDeletionScreenEffect
import io.chefbook.navigation.navigators.BaseNavigator
import io.chefbook.navigation.styles.DismissibleDialog
import org.koin.androidx.compose.koinViewModel

@Destination(route = "profile/deletion", style = DismissibleDialog::class)
@Composable
fun ProfileDeletionScreen(
  navigatior: BaseNavigator,
) {
  val context = LocalContext.current

  val viewModel = koinViewModel<ProfileDeletionScreenViewModel>()
  val state = viewModel.state.collectAsStateWithLifecycle()

  ProfileDeletionScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is ProfileDeletionScreenEffect.ToastShown -> context.showToast(effect.message)
        is ProfileDeletionScreenEffect.Closed -> navigatior.navigateUp()
      }
    }
  }
}
