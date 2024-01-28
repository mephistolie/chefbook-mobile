package io.chefbook.features.auth.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed class AuthScreenEffect : MviSideEffect {
  data class ShowMessage(val messageId: Int) : AuthScreenEffect()
  data class OpenErrorDialog(val error: Throwable? = null) : AuthScreenEffect()
  data object OpenHomeScreen : AuthScreenEffect()
}