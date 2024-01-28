package io.chefbook.features.about.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed class AboutScreenEffect : MviSideEffect {
  data object OnBack : AboutScreenEffect()
}
