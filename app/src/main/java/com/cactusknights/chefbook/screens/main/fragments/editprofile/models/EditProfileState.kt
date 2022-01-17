package com.cactusknights.chefbook.screens.main.fragments.editprofile.models

import com.cactusknights.chefbook.models.User

sealed class EditProfileState {
    object Loading : EditProfileState()
    data class ProfileLoaded(val user: User) : EditProfileState()
}