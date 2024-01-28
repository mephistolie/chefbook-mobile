package io.chefbook.sdk.settings.impl.data.repositories

import io.chefbook.sdk.settings.api.external.domain.entities.ProfileMode
import io.chefbook.sdk.settings.api.internal.data.repositories.ProfileModeRepository
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class ProfileModeRepositoryImpl(
  private val settingsRepository: SettingsRepository
) : ProfileModeRepository {

  override fun observeProfileOnlineMode(): Flow<Boolean> =
    settingsRepository.observeSettings().map { it.profileMode == ProfileMode.ONLINE }

  override suspend fun getProfileMode() = settingsRepository.getProfileMode()

  override suspend fun isProfileModeOnline(): Boolean =
    observeProfileOnlineMode().first()
}
