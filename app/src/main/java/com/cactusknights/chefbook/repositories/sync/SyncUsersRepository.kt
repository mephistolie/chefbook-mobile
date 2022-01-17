package com.cactusknights.chefbook.repositories.sync

import com.cactusknights.chefbook.domain.UserRepository
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.models.UserType
import com.cactusknights.chefbook.repositories.local.datasources.LocalUsersDataSource
import com.cactusknights.chefbook.repositories.remote.datasources.RemoteUserDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import java.net.URI
import javax.inject.Inject

class SyncUsersRepository @Inject constructor(
    private val localSource: LocalUsersDataSource,
    private val remoteSource: RemoteUserDataSource,
    private val settings: SyncSettingsRepository
) : UserRepository {

    val user: MutableStateFlow<User?> = MutableStateFlow(null)

    override suspend fun listenToUser(): MutableStateFlow<User?> { return user }

    override suspend fun getUserInfo(): User {
        val localUser = localSource.getUserInfo()

        return if (settings.getUserType() == UserType.REMOTE) {
            val remoteUser = remoteSource.getUserInfo(); user.emit(remoteUser)
            remoteUser
        } else { user.emit(localUser); localUser }
    }

    override suspend fun changeName(username: String) {
        if (settings.getUserType() == UserType.REMOTE) { remoteSource.changeName(username) }
    }

    override suspend fun uploadAvatar(uri: String) {
        if (settings.getUserType() == UserType.REMOTE) { remoteSource.uploadAvatar(uri) }
    }

    override suspend fun deleteAvatar() {
        if (settings.getUserType() == UserType.REMOTE) { remoteSource.deleteAvatar() }
    }
}