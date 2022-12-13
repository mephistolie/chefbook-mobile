package com.cactusknights.chefbook.data.datasources.local

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.ProfileProto
import com.cactusknights.chefbook.core.toEntity
import com.cactusknights.chefbook.core.toProto
import com.cactusknights.chefbook.data.ILocalProfileSource
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.SuccessResult
import com.cactusknights.chefbook.domain.entities.profile.Profile
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take

class LocalProfileSource @Inject constructor(
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
