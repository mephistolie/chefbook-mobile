package io.chefbook.ui.screens.main.mvi

import io.chefbook.libs.mvi.MviSideEffect

sealed class AppEffect : MviSideEffect {
  data object SignedOut : AppEffect()
}
