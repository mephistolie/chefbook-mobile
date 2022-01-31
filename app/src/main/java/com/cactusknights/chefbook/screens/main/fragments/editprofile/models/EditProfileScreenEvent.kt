package com.cactusknights.chefbook.screens.main.fragments.editprofile.models

import android.content.Context

sealed class EditProfileScreenEvent {
    data class UploadAvatar(val uri: String, val context: Context?) : EditProfileScreenEvent()
    object DeleteAvatar : EditProfileScreenEvent()
    data class ChangeName(val name: String) : EditProfileScreenEvent()
}