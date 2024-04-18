package io.chefbook.features.auth.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import io.chefbook.core.android.compose.composables.LaunchedEffect
import io.chefbook.core.android.showToast
import io.chefbook.features.auth.navigation.AuthScreenNavigator
import io.chefbook.features.auth.ui.mvi.AuthScreenEffect
import io.chefbook.features.auth.ui.mvi.AuthScreenIntent
import io.chefbook.navigation.results.dialogs.TwoButtonsDialogResult
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private const val SIGN_OUT_REQUEST = "SIGN_OUT_REQUEST"

@Destination(
  route = "auth",
  deepLinks = [
    DeepLink(uriPattern = "https://chefbook.io/auth/activate?user_id={userId}&code={activationCode}"),
    DeepLink(uriPattern = "https://chefbook.io/auth/reset-password?user_id={userId}&code={passwordResetCode}")
  ],
)
@Composable
fun AuthScreen(
  userId: String? = null,
  activationCode: String? = null,
  passwordResetCode: String? = null,
  navigator: AuthScreenNavigator,
  confirmDialogRecipient: OpenResultRecipient<TwoButtonsDialogResult>,
) {
  val context = LocalContext.current

  val viewModel = koinViewModel<AuthViewModel> {
    parametersOf(userId.orEmpty(), activationCode.orEmpty(), passwordResetCode.orEmpty())
  }
  val state = viewModel.state.collectAsStateWithLifecycle()

  AuthScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent
  )

  confirmDialogRecipient.onNavResult { navResult ->
    if (navResult is NavResult.Value && navResult.value is TwoButtonsDialogResult.RightButtonClicked) {
      when (navResult.value.request) {
        SIGN_OUT_REQUEST -> viewModel.handleIntent(AuthScreenIntent.SignOut)
      }
    }
  }

  LaunchedEffect {
    navigator.popBackStackToCurrent()

    viewModel.effect.collect { effect ->
      when (effect) {
        is AuthScreenEffect.ToastShown -> context.showToast(effect.message)
        is AuthScreenEffect.DashboardOpened -> navigator.openRecipeBookDashboardScreen()
        is AuthScreenEffect.SignOutConfirmationScreenOpened -> navigator.openTwoButtonsDialog(request = SIGN_OUT_REQUEST)
      }
    }
  }
}
