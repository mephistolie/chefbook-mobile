package com.cactusknights.chefbook.data.repositories

import android.content.Context
import com.cactusknights.chefbook.core.LocalPaths.RECIPES_DIR
import com.cactusknights.chefbook.core.LocalPaths.USER_KEY_FILE
import com.cactusknights.chefbook.data.room.ChefBookDatabase
import com.cactusknights.chefbook.domain.entities.settings.Mode
import com.cactusknights.chefbook.domain.interfaces.ISettingsRepo
import com.cactusknights.chefbook.domain.interfaces.ISourceRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class SourceRepo @Inject constructor(
    private val settings: ISettingsRepo,
    private val database: ChefBookDatabase,
    @ApplicationContext private val context: Context
) : ISourceRepo {

    private val remoteAccess = MutableStateFlow(true)

    override suspend fun observeServerAccess(): StateFlow<Boolean> =
        remoteAccess.asStateFlow()

    override suspend fun isOnlineMode(): Boolean =
        settings.getAppMode() == Mode.ONLINE

    override suspend fun useRemoteSource(): Boolean =
        settings.getAppMode() == Mode.ONLINE && remoteAccess.value

    override suspend fun setServerAccess(hasConnection: Boolean) {
        remoteAccess.emit(hasConnection)
    }

    override suspend fun clearLocalData() {
        database.clearAllTables()
        val recipesDir = File(context.filesDir, RECIPES_DIR)
        if (recipesDir.exists()) {
            recipesDir.deleteRecursively()
        }
        val userKey = File(context.filesDir, USER_KEY_FILE)
        if (userKey.exists()) {
            userKey.delete()
        }
    }

}
