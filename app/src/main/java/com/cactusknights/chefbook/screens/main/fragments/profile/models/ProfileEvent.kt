package com.cactusknights.chefbook.screens.main.fragments.profile.models

sealed class ProfileEvent {
    object SignOut : ProfileEvent()
}