package io.chefbook.features.auth.form.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed class AuthScreenEffect : MviSideEffect {
  data class ToastShown(val message: String) : AuthScreenEffect()
  data class ErrorDialogOpened(val error: Throwable? = null) : AuthScreenEffect()
  data object DashboardOpened : AuthScreenEffect()
}
