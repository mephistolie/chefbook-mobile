package com.cactusknights.chefbook.data.repositories

import android.util.Log
import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.data.LocalProfileDataSource
import com.cactusknights.chefbook.data.RemoteProfileDataSource
import com.cactusknights.chefbook.data.sources.local.datasources.LocalProfileDataSourceImpl
import com.cactusknights.chefbook.domain.ProfileRepo
import com.cactusknights.chefbook.models.Profile
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ProfileRepoImpl @Inject constructor(
    @Local private val localSource: LocalProfileDataSource,
    @Remote private val remoteSource: RemoteProfileDataSource,

    private val settings: SettingsManager
) : ProfileRepo {

    val profile: MutableStateFlow<Profile?> = MutableStateFlow(null)

    override suspend fun listenToProfile(): MutableStateFlow<Profile?> { return profile }

    override suspend fun getProfileInfo(): Profile {
        if (settings.getUserType() == SettingsProto.UserType.OFFLINE) {
            val localProfile = localSource.getProfileInfo()
            profile.emit(localProfile)
            return localProfile
        }

        val cachedProfile = localSource.getCachedProfileInfo()
        profile.emit(cachedProfile)
        return try {
            val remoteUser = remoteSource.getProfileInfo()
            profile.emit(remoteUser)
            localSource.cacheProfileInfo(remoteUser)
            remoteUser
        } catch (e: Exception) { cachedProfile }
    }

    override suspend fun changeName(username: String) {
        if (settings.getUserType() == SettingsProto.UserType.ONLINE) { remoteSource.changeName(username) }
    }

    override suspend fun uploadAvatar(uriString: String) {
        if (settings.getUserType() == SettingsProto.UserType.ONLINE) { remoteSource.uploadAvatar(uriString) }
    }

    override suspend fun deleteAvatar() {
        if (settings.getUserType() == SettingsProto.UserType.ONLINE) { remoteSource.deleteAvatar() }
    }
}