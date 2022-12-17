package com.mysty.chefbook.api.profile.domain

import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.profile.domain.entities.Profile
import kotlinx.coroutines.flow.StateFlow

internal interface IProfileRepo {
    suspend fun observeProfile(): StateFlow<Profile?>
    suspend fun getProfile(forceRefresh: Boolean = false): ActionStatus<Profile>
    suspend fun refreshProfile()
    suspend fun changeUsername(username: String): SimpleAction
    suspend fun changePassword(oldPassword: String, newPassword: String): SimpleAction
    suspend fun uploadAvatar(path: String): SimpleAction
    suspend fun deleteAvatar(): SimpleAction
    suspend fun clearLocalData()
}
