package com.cactusknights.chefbook.ui.screens.main.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

sealed class AppEffect : MviSideEffect {
    object SignedOut : AppEffect()
}
