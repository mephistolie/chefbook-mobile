package com.cactusknights.chefbook.core.datastore

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.SettingsProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import javax.inject.Inject

interface SettingsManager {
    suspend fun listenToSettings(): Flow<SettingsProto>
    suspend fun getSettings(): SettingsProto
    suspend fun getDefaultTab(): SettingsProto.Tabs
    suspend fun setDefaultTab(tab: SettingsProto.Tabs)
    suspend fun getDataSourceType(): SettingsProto.DataSource
    suspend fun setDataSourceType(dataSource: SettingsProto.DataSource)
    suspend fun getUserType(): SettingsProto.UserType
    suspend fun setUserType(userType: SettingsProto.UserType)
    suspend fun getAppTheme(): SettingsProto.AppTheme
    suspend fun setAppTheme(appTheme: SettingsProto.AppTheme)
    suspend fun getAppIcon(): SettingsProto.AppIcon
    suspend fun setAppIcon(appIcon: SettingsProto.AppIcon)
}


class DataStoreSettingsManager @Inject constructor(
    private val settings: DataStore<SettingsProto>
): SettingsManager {

    private val saved get() = settings.data.take(1)

    override suspend fun listenToSettings(): Flow<SettingsProto> = settings.data
    override suspend fun getSettings() = saved.first()

    override suspend fun getDataSourceType(): SettingsProto.DataSource = saved.first().dataSource
    override suspend fun setDataSourceType(dataSource: SettingsProto.DataSource) {
        settings.updateData { it.toBuilder().setDataSource(dataSource).build() }
    }

    override suspend fun getUserType(): SettingsProto.UserType = saved.first().userType
    override suspend fun setUserType(userType: SettingsProto.UserType) {
        settings.updateData { it.toBuilder().setUserType(userType).build() }
    }

    override suspend fun getDefaultTab(): SettingsProto.Tabs = saved.first().defaultTab
    override suspend fun setDefaultTab(tab: SettingsProto.Tabs) {
        settings.updateData { it.toBuilder().setDefaultTab(tab).build() }
    }

    override suspend fun getAppTheme(): SettingsProto.AppTheme = saved.first().appTheme
    override suspend fun setAppTheme(appTheme: SettingsProto.AppTheme) {
        settings.updateData { it.toBuilder().setAppTheme(appTheme).build() }
    }

    override suspend fun getAppIcon(): SettingsProto.AppIcon = saved.first().appIcon
    override suspend fun setAppIcon(appIcon: SettingsProto.AppIcon) {
        settings.updateData { it.toBuilder().setAppIcon(appIcon).build() }
    }
}