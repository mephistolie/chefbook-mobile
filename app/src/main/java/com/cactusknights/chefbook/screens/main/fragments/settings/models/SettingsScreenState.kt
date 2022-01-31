package com.cactusknights.chefbook.screens.main.fragments.settings.models

import com.cactusknights.chefbook.SettingsProto

sealed class SettingsScreenState {
    object Loading : SettingsScreenState()
    data class SettingsLoaded(val settings: SettingsProto) : SettingsScreenState()
}