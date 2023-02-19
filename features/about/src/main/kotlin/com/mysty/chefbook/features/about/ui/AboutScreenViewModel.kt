package com.mysty.chefbook.features.about.ui

import com.mysty.chefbook.core.android.mvi.IIntentSideEffectViewModel
import com.mysty.chefbook.core.android.mvi.IntentSideEffectViewModel
import com.mysty.chefbook.features.about.ui.mvi.AboutScreenEffect
import com.mysty.chefbook.features.about.ui.mvi.AboutScreenIntent

internal typealias IAboutScreenViewModel = IIntentSideEffectViewModel<AboutScreenIntent, AboutScreenEffect>

internal class AboutScreenViewModel : IntentSideEffectViewModel<AboutScreenIntent, AboutScreenEffect>() {

    override suspend fun reduceIntent(intent: AboutScreenIntent) {
        when (intent) {
            is AboutScreenIntent.Back -> _effect.emit(AboutScreenEffect.OnBack)
        }
    }

}
