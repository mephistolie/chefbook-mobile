package io.chefbook.ui.screens.main.mvi

import io.chefbook.libs.mvi.MviSideEffect

sealed class RootEffect : MviSideEffect {
  data class SignedIn(val profileId: String) : RootEffect()
  data object SignedOut : RootEffect()
}
