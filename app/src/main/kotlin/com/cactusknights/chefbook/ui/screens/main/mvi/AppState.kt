package com.cactusknights.chefbook.ui.screens.main.mvi

import com.mysty.chefbook.api.settings.domain.entities.Theme
import com.mysty.chefbook.core.android.mvi.MviState

data class AppState(
    val theme: Theme = Theme.SYSTEM,
) : MviState
