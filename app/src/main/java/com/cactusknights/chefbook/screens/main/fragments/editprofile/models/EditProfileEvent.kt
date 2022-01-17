package com.cactusknights.chefbook.screens.main.fragments.editprofile.models

import android.content.Context

sealed class EditProfileEvent {
    data class UploadAvatar(val uri: String, val context: Context?) : EditProfileEvent()
    object DeleteAvatar : EditProfileEvent()
    data class ChangeName(val name: String) : EditProfileEvent()
}