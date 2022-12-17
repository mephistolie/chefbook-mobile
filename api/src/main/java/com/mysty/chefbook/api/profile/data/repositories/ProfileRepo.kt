package com.mysty.chefbook.api.profile.data.repositories

import com.mysty.chefbook.api.auth.domain.ITokensRepo
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.errors.AppError
import com.mysty.chefbook.api.common.communication.errors.AppErrorType
import com.mysty.chefbook.api.common.communication.errors.ServerError
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.profile.data.ILocalProfileSource
import com.mysty.chefbook.api.profile.data.IRemoteProfileSource
import com.mysty.chefbook.api.profile.domain.IProfileRepo
import com.mysty.chefbook.api.profile.domain.entities.Profile
import com.mysty.chefbook.api.sources.domain.ISourcesRepo
import com.mysty.chefbook.core.coroutines.CoroutineScopes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

internal class ProfileRepo(
    private val localSource: ILocalProfileSource,
    private val remoteSource: IRemoteProfileSource,

    private val source: ISourcesRepo,
    private val session: ITokensRepo,
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
                    Timber.i("Profile signed out")
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

    override suspend fun refreshProfile() {
        getProfile(forceRefresh = true)
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
