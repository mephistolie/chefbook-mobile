package io.chefbook.features.profile.control.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import io.chefbook.features.profile.ProfileComponent
import io.chefbook.features.profile.control.ui.mvi.ProfileScreenEffect
import io.chefbook.features.profile.control.ui.mvi.ProfileScreenIntent
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
  component: ProfileComponent,
) {
  val viewModel = koinViewModel<ProfileScreenViewModel>()
  val state = viewModel.state.collectAsStateWithLifecycle()

  val context = LocalContext.current

  ProfileScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

//  confirmDialogResult.onNavResult { navResult ->
//    if (navResult is NavResult.Value && navResult.value is TwoButtonsDialogResult.RightButtonClicked) {
//      when (navResult.value.request) {
//        LOGOUT_REQUEST -> viewModel.handleIntent(ProfileScreenIntent.SignOut)
//      }
//    }
//  }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is ProfileScreenEffect.Back -> Unit
        is ProfileScreenEffect.RequestLogout -> viewModel.handleIntent(ProfileScreenIntent.SignOut)
//          navigator.openTwoButtonsDialog(
//          TwoButtonsDialogParams(
//            descriptionId = R.string.common_profile_screen_logout_warning,
//            rightButtonIconId = designR.strings.ic_logout,
//          ),
//          request = LOGOUT_REQUEST,
//        )

        is ProfileScreenEffect.ProfileEditingScreenOpened -> Unit
        is ProfileScreenEffect.AppSettingsScreenOpen -> Unit
        is ProfileScreenEffect.AboutAppScreenOpened -> Unit
        is ProfileScreenEffect.UrlOpened -> {
          val urlIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(effect.url)
          )
          context.startActivity(urlIntent)
        }
      }
    }
  }
}
