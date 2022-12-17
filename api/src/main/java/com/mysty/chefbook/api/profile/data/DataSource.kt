package com.mysty.chefbook.api.profile.data

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.profile.domain.entities.Profile

internal interface IProfileSource {
    suspend fun getProfileInfo(): ActionStatus<Profile>
}

internal interface ILocalProfileSource : IProfileSource {
    suspend fun getCachedProfileInfo(): ActionStatus<Profile>
    suspend fun cacheProfileInfo(info: Profile): SimpleAction
    suspend fun deleteProfileCache()
}

internal interface IRemoteProfileSource : IProfileSource {
    suspend fun changeName(username: String): SimpleAction
    suspend fun changePassword(oldPassword: String, newPassword: String): SimpleAction
    suspend fun uploadAvatar(uriString: String): SimpleAction
    suspend fun deleteAvatar(): SimpleAction
}
