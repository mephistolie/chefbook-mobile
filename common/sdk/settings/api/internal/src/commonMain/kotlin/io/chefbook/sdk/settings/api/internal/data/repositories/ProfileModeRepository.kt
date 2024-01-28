package io.chefbook.sdk.settings.api.internal.data.repositories

import io.chefbook.sdk.settings.api.external.domain.entities.ProfileMode
import kotlinx.coroutines.flow.Flow

interface ProfileModeRepository {

  fun observeProfileOnlineMode(): Flow<Boolean>

  suspend fun getProfileMode(): ProfileMode

  suspend fun isProfileModeOnline(): Boolean
}
