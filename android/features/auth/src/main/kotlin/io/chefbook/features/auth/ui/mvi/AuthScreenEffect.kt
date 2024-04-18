package io.chefbook.features.auth.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed interface AuthScreenEffect : MviSideEffect {

  data class ToastShown(val message: String) : AuthScreenEffect

  data object DashboardOpened : AuthScreenEffect

  data object SignOutConfirmationScreenOpened : AuthScreenEffect
}
