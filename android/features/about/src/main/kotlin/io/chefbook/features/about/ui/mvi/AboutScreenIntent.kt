package io.chefbook.features.about.ui.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed class AboutScreenIntent : MviIntent {
  data object Back : AboutScreenIntent()
}