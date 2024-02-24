package io.chefbook.features.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.core.android.showToast
import io.chefbook.features.auth.navigation.AuthScreenNavigator
import io.chefbook.features.auth.ui.mvi.AuthScreenEffect
import org.koin.androidx.compose.koinViewModel
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

  val viewModel: IAuthViewModel = koinViewModel<AuthViewModel> {
    parametersOf(userId.orEmpty(), activationCode.orEmpty(), passwordResetCode.orEmpty())
  }
  val state = viewModel.state.collectAsStateWithLifecycle()

  AuthScreenContent(
    state = state.value,
    onIntent = viewModel::handleIntent
  )

  LaunchedEffect(Unit) {
    navigator.popBackStackToCurrent()

    viewModel.effect.collect { effect ->
      when (effect) {
        is AuthScreenEffect.ToastShown -> context.showToast(effect.message)
        is AuthScreenEffect.DashboardOpened -> navigator.openRecipeBookDashboardScreen()
      }
    }
  }
}
