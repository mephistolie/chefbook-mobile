package io.chefbook.sdk.profile.impl.data.repositories

import io.chefbook.libs.coroutines.CoroutineScopes
import io.chefbook.libs.logger.Logger
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.result.onSuccess
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository
import io.chefbook.sdk.profile.impl.data.sources.local.LocalProfileSource
import io.chefbook.sdk.profile.impl.data.sources.remote.RemoteProfileSource
import io.chefbook.sdk.settings.api.external.domain.entities.ProfileMode
import io.chefbook.sdk.settings.api.internal.data.repositories.ProfileModeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn

internal class ProfileRepositoryImpl(
  private val localSource: LocalProfileSource,
  private val remoteSource: RemoteProfileSource,

  private val profileModeRepository: ProfileModeRepository,
  scopes: CoroutineScopes,
) : ProfileRepository {

  private val profileFlow = localSource.observeProfile()
    .onStart {
      if (profileModeRepository.isProfileModeOnline()) {
        remoteSource.getProfileInfo().onSuccess(localSource::cacheProfileInfo)
      }
    }
    .map { profile ->
      val profileMode = profileModeRepository.getProfileMode()
      val isOnlineProfileMode = profileMode == ProfileMode.ONLINE
      when {
        profileMode == ProfileMode.UNSPECIFIED -> null
        profile.isOnline == isOnlineProfileMode -> profile
        else -> null
      }
    }
    .distinctUntilChanged()
    .shareIn(scopes.repository, SharingStarted.Lazily, replay = 1)

  override fun observeProfile() = profileFlow

  override suspend fun getProfile(): Result<Profile> {
    profileFlow.firstOrNull()?.let { return Result.success(it) }

    val localResult = localSource.getProfileInfo()
    if (localResult.isSuccess) return localResult

    return remoteSource.getProfileInfo()
      .onSuccess { profile ->
        localSource.cacheProfileInfo(profile)
        Result.success(profile)
      }
  }

  override suspend fun getProfileId() =
    getProfile().getOrNull()?.id ?: Profile.LOCAL_PROFILE_ID

  override suspend fun refreshProfile(): EmptyResult {
    if (!profileModeRepository.isProfileModeOnline()) return successResult

    return remoteSource.getProfileInfo()
      .onSuccess(localSource::cacheProfileInfo)
      .asEmpty()
  }

  override suspend fun changeName(username: String): EmptyResult {
    if (!profileModeRepository.isProfileModeOnline()) return Result.failure(Exception("local user"))

    return remoteSource.changeName(username)
      .onSuccess { localSource.updateProfileCache { it.copy(username = username) } }
  }

  override suspend fun uploadAvatar(path: String): EmptyResult {
    return Result.failure(Throwable("not implemented"))
  }

  override suspend fun deleteAvatar(): EmptyResult {
    if (!profileModeRepository.isProfileModeOnline()) return successResult
    return remoteSource.deleteAvatar()
      .onSuccess {
        getProfile().onSuccess {
          localSource.cacheProfileInfo(it.copy(avatar = null))
        }
      }
  }

  override suspend fun clearLocalData() = localSource.clearProfileCache()
}
