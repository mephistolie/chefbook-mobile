package com.cactusknights.chefbook.screens.main.fragments.profile.models

import com.cactusknights.chefbook.models.User

sealed class ProfileState {
    object Loading : ProfileState()
    data class ProfileLoaded(val user: User) : ProfileState()
}