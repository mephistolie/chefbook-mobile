package com.cactusknights.chefbook.data.sources.local.datasources

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.ProfileProto
import com.cactusknights.chefbook.data.LocalProfileDataSource
import com.cactusknights.chefbook.models.Profile
import com.cactusknights.chefbook.models.toProfile
import com.cactusknights.chefbook.models.toProto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import javax.inject.Inject
import javax.inject.Singleton

class LocalProfileDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<ProfileProto>,
): LocalProfileDataSource {

    private val saved get() = dataStore.data.take(1)

    override suspend fun getProfileInfo(): Profile {
        return Profile(
            username = "Local User",
            premium = null,
            isLocal = true,
            isOnline = false,
        )
    }

    override suspend fun getCachedProfileInfo(): Profile = saved.first().toProfile()

    override suspend fun cacheProfileInfo(info: Profile) {
        val inf = info.toProto()
        dataStore.updateData { inf }
    }
}