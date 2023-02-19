package com.mysty.chefbook.api.sources.data.repositories

import com.mysty.chefbook.api.common.constants.LocalPaths
import com.mysty.chefbook.api.common.files.IFileManager
import com.mysty.chefbook.api.common.room.ChefBookDatabase
import com.mysty.chefbook.api.settings.domain.ISettingsRepo
import com.mysty.chefbook.api.settings.domain.entities.Mode
import com.mysty.chefbook.api.sources.domain.ISourcesRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class SourcesRepo(
    private val settings: ISettingsRepo,
    private val database: ChefBookDatabase,
    private val fileManager: IFileManager,
) : ISourcesRepo {

    private var isInitialized = false

    private val remoteAccess = MutableStateFlow(true)

    override suspend fun observeServerAccess(): StateFlow<Boolean> {
        if (!isInitialized) {
            remoteAccess.emit(isOnlineMode())
            isInitialized = true
        }
        return remoteAccess.asStateFlow()
    }

    override suspend fun isOnlineMode(): Boolean =
        settings.getAppMode() == Mode.ONLINE

    override suspend fun useRemoteSource(): Boolean =
        settings.getAppMode() == Mode.ONLINE && remoteAccess.value

    override suspend fun setServerAccess(hasConnection: Boolean) {
        remoteAccess.emit(hasConnection)
    }

    override suspend fun clearLocalData() {
        database.clearAllTables()
        fileManager.removeFile(LocalPaths.RECIPES_DIR)
        fileManager.removeFile(LocalPaths.USER_KEY_FILE)
    }

}
