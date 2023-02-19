package com.mysty.chefbook.api.profile.data.local

import androidx.datastore.core.DataStore
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.SuccessResult
import com.mysty.chefbook.api.profile.data.ILocalProfileSource
import com.mysty.chefbook.api.profile.data.local.dto.ProfileProto
import com.mysty.chefbook.api.profile.data.local.mappers.toEntity
import com.mysty.chefbook.api.profile.data.local.mappers.toProto
import com.mysty.chefbook.api.profile.domain.entities.Profile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take

internal class LocalProfileSource(
    private val dataStore: DataStore<ProfileProto>,
): ILocalProfileSource {

    private val saved get() = dataStore.data.take(1)

    override suspend fun getProfileInfo(): ActionStatus<Profile> =
        DataResult(
            Profile(
                broccoins = 0,
                username = "Local User",
                premium = false,
                isOnline = false,
            )
        )

    override suspend fun getCachedProfileInfo(): ActionStatus<Profile> =
        DataResult(saved.first().toEntity())

    override suspend fun cacheProfileInfo(info: Profile): SimpleAction {
        val inf = info.toProto()
        dataStore.updateData { inf }
        return SuccessResult
    }

    override suspend fun deleteProfileCache() {
        dataStore.updateData { ProfileProto.getDefaultInstance() }
    }

}
