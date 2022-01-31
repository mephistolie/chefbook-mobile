package com.cactusknights.chefbook.screens.main.fragments.settings.models

import android.app.Activity
import com.cactusknights.chefbook.SettingsProto

sealed class SettingsScreenEvent {
    data class SetDataSource(val dataSource: SettingsProto.DataSource) : SettingsScreenEvent()
    data class SetTheme(val theme: SettingsProto.AppTheme) : SettingsScreenEvent()
    data class SetIcon(val icon: SettingsProto.AppIcon, val activity: Activity) : SettingsScreenEvent()
    data class SetDefaultTab(val tab: SettingsProto.Tabs) : SettingsScreenEvent()
}