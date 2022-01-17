package com.cactusknights.chefbook.models

import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

enum class DataSource {
    LOCAL, REMOTE;

    companion object {
        fun getDataSourceByString(input: String): DataSource {
            return if (input.lowercase() == "remote") REMOTE else LOCAL
        }
    }
}

enum class UserType {
    LOCAL, REMOTE;

    companion object {
        fun getUserTypeByString(input: String): UserType {
            return if (input.lowercase() == "remote") REMOTE else LOCAL
        }
    }
}

enum class AppIcon {
    STANDARD, OLD;
}

enum class AppTheme {
    SYSTEM, LIGHT, DARK;
}

data class Settings (
    var userType : UserType = UserType.LOCAL,
    var dataSource: DataSource = DataSource.LOCAL,
    var isShoppingListDefault: Boolean = false,
    var icon : AppIcon = AppIcon.STANDARD,
    var theme : AppTheme = AppTheme.SYSTEM
)

object SettingsScheme {
    val FIELD_USER_TYPE = intPreferencesKey("user_type")
    val FIELD_DATA_SOURCE = intPreferencesKey("data_source")
    val FIELD_SHOPPING_LIST_DEFAULT = booleanPreferencesKey("shopping_list_default")
    val FIELD_ICON = intPreferencesKey("icon")
    val FIELD_THEME = intPreferencesKey("theme")
}