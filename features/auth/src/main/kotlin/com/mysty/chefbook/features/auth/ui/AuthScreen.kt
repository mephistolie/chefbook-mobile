package com.mysty.chefbook.features.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.mysty.chefbook.core.android.showToast
import com.mysty.chefbook.features.auth.ui.mvi.AuthScreenEffect
import com.mysty.chefbook.features.auth.ui.navigation.IAuthScreenNavigator
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel

@Destination(route = "auth")
@Composable
internal fun AuthScreen(
  navigator: IAuthScreenNavigator,
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
        is AuthScreenEffect.OpenHomeScreen -> navigator.openHomeScreen()
      }
    }
  }
}
