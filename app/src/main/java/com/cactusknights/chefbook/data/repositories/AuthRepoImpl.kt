package com.cactusknights.chefbook.data.repositories

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.SyncDataProto
import com.cactusknights.chefbook.core.cache.CategoriesCacheWriter
import com.cactusknights.chefbook.core.cache.RecipeBookCacheWriter
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.core.room.ChefBookDatabase
import com.cactusknights.chefbook.data.AuthDataSource
import com.cactusknights.chefbook.domain.AuthRepo
import com.cactusknights.chefbook.data.sources.remote.datasources.RemoteAuthDataSourceImpl
import com.cactusknights.chefbook.di.Remote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

class AuthRepoImpl @Inject constructor(
    @Remote private val remoteSource: AuthDataSource,

    private val settings: SettingsManager,
    private val syncData: DataStore<SyncDataProto>,
    private val recipesCache: RecipeBookCacheWriter,
    private val categoriesCache: CategoriesCacheWriter,
    private val database: ChefBookDatabase
) : AuthRepo {

    override suspend fun signUp(email: String, password: String) {
        remoteSource.signUp(email, password)
    }

    override suspend fun signIn(email: String, password: String): Boolean {
        val signedIn = remoteSource.signIn(email, password)
        if (signedIn) {
            settings.setUserType(SettingsProto.UserType.ONLINE)
            settings.setDataSourceType(SettingsProto.DataSource.REMOTE)
        }
        return signedIn
    }

    override suspend fun signInLocally() {
        settings.setUserType(SettingsProto.UserType.OFFLINE)
        settings.setDataSourceType(SettingsProto.DataSource.LOCAL)
    }

    override suspend fun signOut() {
        settings.setUserType(SettingsProto.UserType.OFFLINE)
        settings.setDataSourceType(SettingsProto.DataSource.LOCAL)
        remoteSource.signOut()
        CoroutineScope(Dispatchers.IO).launch { clearLocalData() }
    }

    private suspend fun clearLocalData() {
        recipesCache.setRecipes(arrayListOf())
        categoriesCache.setCategories(arrayListOf())
        syncData.updateData { SyncDataProto.getDefaultInstance() }
        database.clearAllTables()
    }
}