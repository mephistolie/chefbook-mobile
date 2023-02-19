package com.mysty.chefbook.features.about.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

internal sealed class AboutScreenEffect : MviSideEffect {
    object OnBack : AboutScreenEffect()
}
