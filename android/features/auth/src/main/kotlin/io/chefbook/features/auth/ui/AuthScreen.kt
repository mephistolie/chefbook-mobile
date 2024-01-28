package io.chefbook.features.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import io.chefbook.features.auth.ui.mvi.AuthScreenEffect
import io.chefbook.features.auth.navigation.AuthScreenNavigator
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.core.android.showToast
import org.koin.androidx.compose.getViewModel

@Destination(route = "auth")
@Composable
fun AuthScreen(
  navigator: AuthScreenNavigator,
) {
  val context = LocalContext.current

  val authScreenViewModel: IAuthViewModel = getViewModel<AuthViewModel>()
  val authScreenState = authScreenViewModel.state.collectAsState()

  AuthScreenContent(
    state = authScreenState.value,
    onIntent = authScreenViewModel::handleIntent
  )

  LaunchedEffect(Unit) {
    authScreenViewModel.effect.collect { effect ->
      when (effect) {
        is AuthScreenEffect.ShowMessage -> context.showToast(effect.messageId)
        is AuthScreenEffect.OpenErrorDialog -> navigator.openErrorInfoDialog(error = effect.error)
        is AuthScreenEffect.OpenHomeScreen -> navigator.openDashboardScreen()
      }
    }
  }
}
