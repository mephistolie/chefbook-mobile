package com.cactusknights.chefbook.repositories.sync

import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.domain.UserRepository
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.repositories.local.datasources.LocalUsersDataSource
import com.cactusknights.chefbook.repositories.remote.datasources.RemoteUserDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncUsersRepository @Inject constructor(
    private val localSource: LocalUsersDataSource,
    private val remoteSource: RemoteUserDataSource,
    private val settings: SettingsManager
) : UserRepository {

    val user: MutableStateFlow<User?> = MutableStateFlow(null)

    override suspend fun listenToUser(): MutableStateFlow<User?> { return user }

    override suspend fun getUserInfo(): User {
        val localUser = localSource.getUserInfo()

        return if (settings.getUserType() == SettingsProto.UserType.ONLINE) {
            val remoteUser = remoteSource.getUserInfo(); user.emit(remoteUser)
            remoteUser
        } else { user.emit(localUser); localUser }
    }

    override suspend fun changeName(username: String) {
        if (settings.getUserType() == SettingsProto.UserType.ONLINE) { remoteSource.changeName(username) }
    }

    override suspend fun uploadAvatar(uri: String) {
        if (settings.getUserType() == SettingsProto.UserType.ONLINE) { remoteSource.uploadAvatar(uri) }
    }

    override suspend fun deleteAvatar() {
        if (settings.getUserType() == SettingsProto.UserType.ONLINE) { remoteSource.deleteAvatar() }
    }
}