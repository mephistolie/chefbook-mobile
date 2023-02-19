package com.mysty.chefbook.features.home.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

internal sealed class HomeScreenEffect : MviSideEffect {
    object ProfileOpened: HomeScreenEffect()
}
