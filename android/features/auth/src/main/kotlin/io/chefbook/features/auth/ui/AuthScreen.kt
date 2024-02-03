package io.chefbook.features.auth.form.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.annotation.DeepLink
import io.chefbook.features.auth.form.ui.mvi.AuthScreenEffect
import io.chefbook.features.auth.form.navigation.AuthScreenNavigator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import io.chefbook.core.android.showToast
import io.chefbook.features.auth.form.ui.mvi.AuthScreenIntent
import io.chefbook.libs.logger.Logger
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

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
) {
  val context = LocalContext.current

  val viewModel: IAuthViewModel = getViewModel<AuthViewModel> {
    parametersOf(userId.orEmpty(), activationCode.orEmpty(), passwordResetCode.orEmpty())
  }
  val state = viewModel.state.collectAsState()

  AuthScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent
  )

  LaunchedEffect(Unit) {
    navigator.popBackStackToCurrent()

    viewModel.effect.collect { effect ->
      when (effect) {
        is AuthScreenEffect.ToastShown -> context.showToast(effect.message)
        is AuthScreenEffect.ErrorDialogOpened -> navigator.openErrorInfoDialog(error = effect.error)
        is AuthScreenEffect.DashboardOpened -> navigator.openDashboardScreen()
      }
    }
  }
}
