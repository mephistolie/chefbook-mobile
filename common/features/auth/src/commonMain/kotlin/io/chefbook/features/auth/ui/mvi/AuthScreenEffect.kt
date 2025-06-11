package io.chefbook.features.auth.ui.mvi

sealed interface AuthScreenEffect {

  data class ToastShown(val message: String) : AuthScreenEffect

  data object DashboardOpened : AuthScreenEffect

  data object SignOutConfirmationScreenOpened : AuthScreenEffect
}
