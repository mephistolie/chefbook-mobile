package com.mysty.chefbook.features.auth.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviSideEffect

internal sealed class AuthScreenEffect : MviSideEffect {
    data class ShowMessage(val messageId: Int) : AuthScreenEffect()
    data class OpenErrorDialog(val error: Throwable? = null) : AuthScreenEffect()
    object OpenHomeScreen : AuthScreenEffect()
}