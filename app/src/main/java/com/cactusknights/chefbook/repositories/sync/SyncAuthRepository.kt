package com.cactusknights.chefbook.repositories.sync

import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.domain.AuthRepository
import com.cactusknights.chefbook.repositories.remote.datasources.RemoteAuthDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncAuthRepository @Inject constructor(
    private val remoteSource: RemoteAuthDataSource,
    private val settings: SettingsManager
) : AuthRepository {

    override suspend fun signUp(email: String, password: String) {
        remoteSource.signUp(email, password)
        settings.setUserType(SettingsProto.UserType.ONLINE); settings.setDataSourceType(SettingsProto.DataSource.REMOTE)
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
        settings.setUserType(SettingsProto.UserType.OFFLINE); settings.setDataSourceType(SettingsProto.DataSource.LOCAL)
    }

    override suspend fun signOut() {
        settings.setUserType(SettingsProto.UserType.OFFLINE); settings.setDataSourceType(SettingsProto.DataSource.LOCAL); remoteSource.signOut()
    }
}