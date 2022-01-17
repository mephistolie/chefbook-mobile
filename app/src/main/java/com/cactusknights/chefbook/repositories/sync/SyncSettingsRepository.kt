package com.cactusknights.chefbook.repositories.sync

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.cactusknights.chefbook.domain.SettingsRepository
import com.cactusknights.chefbook.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class SyncSettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
): SettingsRepository {

    private val settings = Settings()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.data.collect { prefs ->
                settings.dataSource = if (prefs[SettingsScheme.FIELD_DATA_SOURCE]?:0 == 0) DataSource.LOCAL else DataSource.REMOTE
                settings.userType = if (prefs[SettingsScheme.FIELD_USER_TYPE]?:0 == 0) UserType.LOCAL else UserType.REMOTE
                settings.isShoppingListDefault = prefs[SettingsScheme.FIELD_SHOPPING_LIST_DEFAULT]?:false
                settings.theme = when(prefs[SettingsScheme.FIELD_THEME]?:0) {
                    1 -> AppTheme.LIGHT
                    2 -> AppTheme.DARK
                    else -> AppTheme.SYSTEM
                }
                settings.icon = when(prefs[SettingsScheme.FIELD_ICON]?:0) {
                    1 -> AppIcon.OLD
                    else -> AppIcon.STANDARD
                }
            }
        }
    }

    override fun getSettings(): Settings { return settings }

    override fun getShoppingListByDefault(): Boolean { return settings.isShoppingListDefault }

    override suspend fun setShoppingListByDefault(isShoppingListDefault: Boolean) {
        dataStore.edit {
            it[SettingsScheme.FIELD_SHOPPING_LIST_DEFAULT] = isShoppingListDefault
        }
    }

    override fun getDataSourceType(): DataSource {
        return settings.dataSource
    }

    override suspend fun setDataSourceType(dataSource: DataSource) {
        dataStore.edit {
            it[SettingsScheme.FIELD_DATA_SOURCE] = if (dataSource == DataSource.LOCAL) 0 else 1
        }
    }

    override fun getUserType(): UserType {
        return settings.userType
    }

    override suspend fun setUserType(userType: UserType) {
        dataStore.edit {
            it[SettingsScheme.FIELD_USER_TYPE] = if (userType == UserType.LOCAL) 0 else 1
        }
    }

    override fun getAppTheme(): AppTheme {
        return settings.theme
    }

    override suspend fun setAppTheme(appTheme: AppTheme) {
        dataStore.edit {
            it[SettingsScheme.FIELD_THEME] = when (appTheme) {
                AppTheme.LIGHT -> 1
                AppTheme.DARK -> 2
                else -> 0
            }
        }
    }

    override fun getAppIcon(): AppIcon {
        return settings.icon
    }

    override suspend fun setAppIcon(appIcon: AppIcon) {
        dataStore.edit {
            it[SettingsScheme.FIELD_ICON] = when (appIcon) {
                AppIcon.OLD -> 1
                else -> 0
            }
        }
    }
}