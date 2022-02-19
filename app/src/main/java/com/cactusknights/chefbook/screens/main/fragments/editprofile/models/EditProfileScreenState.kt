package com.cactusknights.chefbook.screens.main.fragments.editprofile.models

import com.cactusknights.chefbook.models.Profile

sealed class EditProfileScreenState {
    object Loading : EditProfileScreenState()
    data class ProfileLoaded(val user: Profile) : EditProfileScreenState()
}