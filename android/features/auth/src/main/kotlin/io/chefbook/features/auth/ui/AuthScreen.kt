package io.chefbook.features.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.chefbook.utils.android.showToast
import io.chefbook.ui.utils.compose.composables.LaunchedEffect
import io.chefbook.features.auth.AuthComponent
import io.chefbook.features.auth.ui.mvi.AuthScreenEffect
import io.chefbook.navigation.viewModelStoreOwner
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private const val SIGN_OUT_REQUEST = "SIGN_OUT_REQUEST"

@Composable
fun AuthScreen(
  component: AuthComponent,
//  userId: String? = null,
//  activationCode: String? = null,
//  passwordResetCode: String? = null,
) {
  val context = LocalContext.current

  val viewModel = koinViewModel<AuthViewModel>(viewModelStoreOwner = component.viewModelStoreOwner) { parametersOf("", "", "") }
  val state = viewModel.state.collectAsStateWithLifecycle()

  AuthScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent,
  )

//  confirmDialogRecipient.onNavResult { navResult ->
//    if (navResult is NavResult.Value && navResult.value is TwoButtonsDialogResult.RightButtonClicked) {
//      when (navResult.value.request) {
//        SIGN_OUT_REQUEST -> viewModel.handleIntent(AuthScreenIntent.SignOut)
//      }
//    }
//  }

  LaunchedEffect {
    viewModel.effect.collect { effect ->
      when (effect) {
        is AuthScreenEffect.ToastShown -> context.showToast(effect.message)
        is AuthScreenEffect.DashboardOpened -> Unit // navigator.openRecipeBookDashboardScreen()
        is AuthScreenEffect.SignOutConfirmationScreenOpened -> Unit // navigator.openTwoButtonsDialog(request = SIGN_OUT_REQUEST)
      }
    }
  }
}
