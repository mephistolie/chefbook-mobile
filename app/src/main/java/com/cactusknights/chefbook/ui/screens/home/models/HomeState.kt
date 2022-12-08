package com.cactusknights.chefbook.ui.screens.home.models

import com.cactusknights.chefbook.domain.entities.settings.Tab

data class HomeState(
    val currentTab: Tab = Tab.RECIPE_BOOK,
)
