package com.cactusknights.chefbook.ui.screens.main.models

import com.cactusknights.chefbook.domain.entities.profile.Profile
import com.cactusknights.chefbook.domain.entities.settings.Settings

data class AppState(
    val profile: Profile = Profile(),
    val settings: Settings? = null,
)
