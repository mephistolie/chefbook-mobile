package com.cactusknights.chefbook.repositories.sync

import android.content.SharedPreferences
import com.cactusknights.chefbook.domain.AuthRepository
import com.cactusknights.chefbook.models.DataSource
import com.cactusknights.chefbook.models.UserType
import com.cactusknights.chefbook.repositories.remote.datasources.RemoteAuthDataSource
import javax.inject.Inject

class SyncAuthRepository @Inject constructor(
    private val remoteSource: RemoteAuthDataSource,
    private val settings: SyncSettingsRepository
) : AuthRepository {

    override suspend fun signUp(email: String, password: String) {
        remoteSource.signUp(email, password)
        settings.setUserType(UserType.REMOTE); settings.setDataSourceType(DataSource.REMOTE)
    }

    override suspend fun signIn(email: String, password: String): Boolean {
        val signedIn = remoteSource.signIn(email, password)
        if (signedIn) {
            settings.setUserType(UserType.REMOTE)
            settings.setDataSourceType(DataSource.REMOTE)
        }
        return signedIn
    }

    override suspend fun signInLocally() {
        settings.setUserType(UserType.LOCAL); settings.setDataSourceType(DataSource.LOCAL)
    }

    override suspend fun signOut() {
        settings.setUserType(UserType.LOCAL); settings.setDataSourceType(DataSource.LOCAL); remoteSource.signOut()
    }
}