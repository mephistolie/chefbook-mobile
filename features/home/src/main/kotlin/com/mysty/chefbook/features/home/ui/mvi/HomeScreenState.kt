package com.mysty.chefbook.features.home.ui.mvi

import com.mysty.chefbook.api.settings.domain.entities.Tab
import com.mysty.chefbook.core.android.mvi.MviState

data class HomeScreenState(
    val currentTab: Tab = Tab.RECIPE_BOOK,
    val profileAvatar: String? = null,
) : MviState
