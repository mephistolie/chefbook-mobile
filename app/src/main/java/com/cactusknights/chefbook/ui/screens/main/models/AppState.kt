package com.cactusknights.chefbook.ui.screens.main.models

import com.mysty.chefbook.api.profile.domain.entities.Profile
import com.mysty.chefbook.api.settings.domain.entities.Settings

data class AppState(
    val profile: Profile = Profile(),
    val settings: Settings? = null,
)
