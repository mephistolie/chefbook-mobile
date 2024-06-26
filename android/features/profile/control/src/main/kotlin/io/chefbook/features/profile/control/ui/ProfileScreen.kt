package io.chefbook.features.profile.control.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import io.chefbook.features.profile.control.R
import io.chefbook.features.profile.control.navigation.ProfileScreenNavigator
import io.chefbook.features.profile.control.ui.mvi.ProfileScreenEffect
import io.chefbook.features.profile.control.ui.mvi.ProfileScreenIntent
import io.chefbook.navigation.params.dialogs.TwoButtonsDialogParams
import io.chefbook.navigation.results.dialogs.TwoButtonsDialogResult
import org.koin.androidx.compose.koinViewModel
import io.chefbook.design.R as designR

private const val LOGOUT_REQUEST = "LOGOUT_REQUEST"

@Destination(route = "profile")
@Composable
fun ProfileScreen(
  navigator: ProfileScreenNavigator,
  confirmDialogResult: OpenResultRecipient<TwoButtonsDialogResult>,
) {
  val viewModel = koinViewModel<ProfileScreenViewModel>()
  val state = viewModel.state.collectAsStateWithLifecycle()

  val context = LocalContext.current

  ProfileScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

  confirmDialogResult.onNavResult { navResult ->
    if (navResult is NavResult.Value && navResult.value is TwoButtonsDialogResult.RightButtonClicked) {
      when (navResult.value.request) {
        LOGOUT_REQUEST -> viewModel.handleIntent(ProfileScreenIntent.SignOut)
      }
    }
  }

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is ProfileScreenEffect.Back -> navigator.navigateUp()
        is ProfileScreenEffect.RequestLogout -> navigator.openTwoButtonsDialog(
          TwoButtonsDialogParams(
            descriptionId = R.string.common_profile_screen_logout_warning,
            rightButtonIconId = designR.drawable.ic_logout,
          ),
          request = LOGOUT_REQUEST,
        )

        is ProfileScreenEffect.ProfileEditingScreenOpened -> navigator.openProfileEditingScreen()
        is ProfileScreenEffect.AppSettingsScreenOpen -> navigator.openAppSettingsScreen()
        is ProfileScreenEffect.AboutAppScreenOpened -> navigator.openAboutAppScreen()
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
