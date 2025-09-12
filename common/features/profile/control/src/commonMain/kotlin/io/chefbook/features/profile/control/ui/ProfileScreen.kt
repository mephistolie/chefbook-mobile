package io.chefbook.features.profile.control.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import io.chefbook.features.profile.control.component.ProfileComponent
import io.chefbook.features.profile.control.mvi.ProfileEffect
import io.chefbook.features.profile.control.mvi.ProfileIntent

@Composable
fun ProfileScreen(
  component: ProfileComponent,
) {
  LaunchedEffect(Unit) {
    component.store.sideEffectsFlow.collect { effect ->
      when (effect) {
        is ProfileEffect.Back -> Unit
        is ProfileEffect.RequestLogout -> component.store.handle(ProfileIntent.SignOut)
//          navigator.openTwoButtonsDialog(
//          TwoButtonsDialogParams(
//            descriptionId = R.string.common_profile_screen_logout_warning,
//            rightButtonIconId = designR.strings.ic_logout,
//          ),
//          request = LOGOUT_REQUEST,
//        )

        is ProfileEffect.ProfileEditingScreenOpened -> Unit
        is ProfileEffect.AppSettingsScreenOpen -> Unit
        is ProfileEffect.AboutAppScreenOpened -> Unit
        is ProfileEffect.UrlOpened -> {
//          val urlIntent = Intent(
//            Intent.ACTION_VIEW,
//            Uri.parse(effect.url)
//          )
//          context.startActivity(urlIntent)
        }
      }
    }
  }

  val state = component.store.stateFlow.collectAsState()
  ProfileScreenContent(
    state = state.value,
    onIntent = component.store::handle,
  )

//  val context = LocalContext.current


//  confirmDialogResult.onNavResult { navResult ->
//    if (navResult is NavResult.Value && navResult.value is TwoButtonsDialogResult.RightButtonClicked) {
//      when (navResult.value.request) {
//        LOGOUT_REQUEST -> viewModel.handleIntent(ProfileScreenIntent.SignOut)
//      }
//    }
//  }
}
