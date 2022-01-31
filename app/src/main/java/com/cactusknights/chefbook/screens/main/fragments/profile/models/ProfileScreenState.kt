package com.cactusknights.chefbook.screens.main.fragments.profile.models

import com.cactusknights.chefbook.models.User

sealed class ProfileScreenState {
    object Loading : ProfileScreenState()
    data class ProfileLoaded(val user: User) : ProfileScreenState()
}