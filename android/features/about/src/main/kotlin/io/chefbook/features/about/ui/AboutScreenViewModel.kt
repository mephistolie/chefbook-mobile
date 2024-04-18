package io.chefbook.features.about.ui

import io.chefbook.features.about.ui.mvi.AboutScreenEffect
import io.chefbook.features.about.ui.mvi.AboutScreenIntent
import io.chefbook.libs.mvi.BaseIntentSideEffectViewModel
import io.chefbook.libs.mvi.IntentSideEffectViewModel

internal class AboutScreenViewModel :
  BaseIntentSideEffectViewModel<AboutScreenIntent, AboutScreenEffect>() {

  override suspend fun reduceIntent(intent: AboutScreenIntent) {
    when (intent) {
      is AboutScreenIntent.Back -> _effect.emit(AboutScreenEffect.OnBack)
    }
  }
}
