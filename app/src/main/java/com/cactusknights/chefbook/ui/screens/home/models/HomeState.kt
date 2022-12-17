package com.cactusknights.chefbook.ui.screens.home.models

import com.mysty.chefbook.api.settings.domain.entities.Tab

data class HomeState(
    val currentTab: Tab = Tab.RECIPE_BOOK,
)
