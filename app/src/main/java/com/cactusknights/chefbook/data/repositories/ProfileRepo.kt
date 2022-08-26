package com.cactusknights.chefbook.data.repositories

import com.cactusknights.chefbook.core.coroutines.CoroutineScopes
import com.cactusknights.chefbook.data.ILocalProfileSource
import com.cactusknights.chefbook.data.IRemoteProfileSource
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.AppError
import com.cactusknights.chefbook.domain.entities.action.AppErrorType
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.Failure
import com.cactusknights.chefbook.domain.entities.action.ServerError
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isSuccess
import com.cactusknights.chefbook.domain.entities.profile.Profile
import com.cactusknights.chefbook.domain.interfaces.IProfileRepo
import com.cactusknights.chefbook.domain.interfaces.ISessionRepo
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Singleton
class ProfileRepo @Inject constructor(
    @Local private val localSource: ILocalProfileSource,
    @Remote private val remoteSource: IRemoteProfileSource,

    private val source: SourceRepo,
    private val session: ISessionRepo,
    private val scopes: CoroutineScopes
) : IProfileRepo {

    private val profile: MutableStateFlow<Profile?> = MutableStateFlow(null)
    private var isInitialized = false

    init {
        initProfile()
        handleSignOut()
    }

    private fun initProfile() {
        scopes.repository.launch {
            getProfile()
            isInitialized = true
        }
    }

    private fun handleSignOut() {
        scopes.repository.launch {
            session.observeSession().collect { session ->
                if (session == null && source.isOnlineMode()) {
                    profile.emit(null)
                    source.clearLocalData()
                }
            }
        }
    }

    override suspend fun observeProfile(): StateFlow<Profile?> {
        while (!isInitialized) {
            delay(100)
        }
        return profile
    }

    override suspend fun getProfile(forceRefresh: Boolean): ActionStatus<Profile> {
        if (!forceRefresh) profile.value?.let { return DataResult(it) }
        return if (source.isOnlineMode()) {
            var result = remoteSource.getProfileInfo()

            if (result.isSuccess()) {
                profile.emit(result.data())
                localSource.cacheProfileInfo(result.data())
            } else if (result.asFailure().error !is ServerError) {
                result = localSource.getCachedProfileInfo()
                if (result.isSuccess()) {
                    profile.emit(result.data())
                }
            }

            result
        } else {
            val result = localSource.getProfileInfo()

            if (result.isSuccess()) {
                profile.emit(result.data())
            }

            result
        }
    }

    override suspend fun changeUsername(username: String): SimpleAction {
        if (!source.isOnlineMode()) return Failure(AppError(AppErrorType.LOCAL_USER))

        val result = remoteSource.changeName(username)
        if (result.isSuccess()) {
            val updatedProfile = profile.value?.copy(avatar = null)
            profile.emit(updatedProfile)
            updatedProfile?.let { localSource.cacheProfileInfo(it) }
        }

        return result
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): SimpleAction {
        if (!source.isOnlineMode()) return Failure(AppError(AppErrorType.LOCAL_USER))

        return remoteSource.changePassword(oldPassword, newPassword)
    }

    override suspend fun uploadAvatar(path: String): SimpleAction {
        if (!source.isOnlineMode()) return Failure(AppError(AppErrorType.LOCAL_USER))

        val result = remoteSource.uploadAvatar(path)
        if (result.isSuccess()) {
            val updatedProfile = profile.value?.copy(avatar = null)
            profile.emit(updatedProfile)
            updatedProfile?.let { localSource.cacheProfileInfo(it) }
        }

        return result
    }

    override suspend fun deleteAvatar(): SimpleAction {
        if (!source.isOnlineMode()) return Failure(AppError(AppErrorType.LOCAL_USER))

        val result = remoteSource.deleteAvatar()
        if (result.isSuccess()) {
            val updatedProfile = profile.value?.copy(avatar = null)
            profile.emit(updatedProfile)
            updatedProfile?.let { localSource.cacheProfileInfo(it) }
        }

        return result
    }

    override suspend fun clearLocalData() = localSource.deleteProfileCache()

}
