package io.chefbook.features.root.ui.mvi

sealed class RootSideEffect {
  data class SignedIn(val profileId: String) : RootSideEffect()
  data object SignedOut : RootSideEffect()
}
