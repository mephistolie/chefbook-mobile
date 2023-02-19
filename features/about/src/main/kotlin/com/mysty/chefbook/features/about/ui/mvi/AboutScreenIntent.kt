package com.mysty.chefbook.features.about.ui.mvi

import com.mysty.chefbook.core.android.mvi.MviIntent

internal sealed class AboutScreenIntent : MviIntent {
    object Back : AboutScreenIntent()
}