package com.cactusknights.chefbook.screens.main.fragments.profile.models

import com.cactusknights.chefbook.models.Profile

sealed class ProfileScreenState {
    object Loading : ProfileScreenState()
    data class ProfileLoaded(val user: Profile) : ProfileScreenState()
}