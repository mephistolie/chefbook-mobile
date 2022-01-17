package com.cactusknights.chefbook.screens.main.fragments.settings.models

import android.app.Activity
import com.cactusknights.chefbook.models.AppIcon
import com.cactusknights.chefbook.models.AppTheme
import com.cactusknights.chefbook.models.DataSource

sealed class SettingsEvent {
    data class SetDataSource(val dataSource: DataSource) : SettingsEvent()
    data class SetTheme(val theme: AppTheme) : SettingsEvent()
    data class SetIcon(val icon: AppIcon, val activity: Activity) : SettingsEvent()
    data class SetShoppingListDefault(val isDefault: Boolean) : SettingsEvent()
}