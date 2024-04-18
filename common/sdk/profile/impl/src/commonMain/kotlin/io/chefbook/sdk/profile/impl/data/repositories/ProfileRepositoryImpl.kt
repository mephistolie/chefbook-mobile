package io.chefbook.sdk.profile.impl.data.repositories

import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.coroutines.CoroutineScopes
import io.chefbook.libs.logger.Logger
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.result.onSuccess
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.file.api.internal.data.repositories.FileRepository
import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository
import io.chefbook.sdk.profile.api.internal.data.sources.local.LocalProfileSource
import io.chefbook.sdk.profile.impl.data.sources.remote.RemoteProfileSource
import io.chefbook.sdk.settings.api.external.domain.entities.ProfileMode
import io.chefbook.sdk.settings.api.internal.data.repositories.ProfileModeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ProfileRepositoryImpl(
  private val localSource: LocalProfileSource,
  private val remoteSource: RemoteProfileSource,

  private val profileModeRepository: ProfileModeRepository,
  private val files: FileRepository,
  private val dispatchers: AppDispatchers,
  scopes: CoroutineScopes,
) : ProfileRepository {

  private val profileFlow = localSource.observeProfile()
    .onStart {
      scopes.repository.launch {
        if (profileModeRepository.isProfileModeOnline()) {
          remoteSource.getProfileInfo().onSuccess(localSource::cacheProfileInfo)
        }
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

  override suspend fun uploadAvatar(path: String): EmptyResult = withContext(dispatchers.io) {
    val targetSource = if (profileModeRepository.isProfileModeOnline()) remoteSource else localSource
    val uploadingResult = targetSource.generateAvatarUploading()
      .onFailure { return@withContext Result.failure(it) }
    val uploading = uploadingResult.getOrThrow()

    val compressedPath = files.compressImage(
      path = path,
      width = 512, height = 512,
      maxFileSize = uploading.maxSize,
    ).onFailure { return@withContext Result.failure(it) }

    val fileResult = files.getFile(compressedPath.getOrThrow()).onFailure { return@withContext Result.failure(it) }

    val file = fileResult.getOrThrow()

    files.uploadFile(
      path = uploading.uploadPath,
      file = file,
      meta = uploading.meta,
    ).onFailure { return@withContext Result.failure(it) }


    return@withContext if (profileModeRepository.isProfileModeOnline()) {
      remoteSource.confirmAvatarUploading(uploading.picturePath)
        .onSuccess { localSource.confirmAvatarUploading(uploading.picturePath) }
    } else {
      localSource.confirmAvatarUploading(uploading.picturePath)
    }
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

  override suspend fun checkNicknameAvailability(nickname: String) =
    remoteSource.checkNicknameAvailability(nickname)

  override suspend fun setNickname(nickname: String) =
    remoteSource.setNickname(nickname)
      .onSuccess { localSource.updateProfileCache { it.copy(nickname = nickname) } }

  override suspend fun setName(firstName: String?, lastName: String?): EmptyResult {
    return if (profileModeRepository.isProfileModeOnline()) {
      remoteSource.setName(firstName, lastName).onSuccess {
        localSource.updateProfileCache { it.copy(firstName = firstName, lastName = lastName) }
      }
    } else {
      localSource.updateProfileCache { it.copy(firstName = firstName, lastName = lastName) }
      successResult
    }
  }

  override suspend fun setDescription(description: String?): EmptyResult {
    return if (profileModeRepository.isProfileModeOnline()) {
      remoteSource.setDescription(description).onSuccess {
        localSource.updateProfileCache { it.copy(description = description) }
      }
    } else {
      localSource.updateProfileCache { it.copy(description = description) }
      successResult
    }
  }

  override suspend fun requestProfileDeletion(password: String, deleteSharedData: Boolean) =
    remoteSource.requestProfileDeletion(password, deleteSharedData)

  override suspend fun cancelProfileDeletion() =
    remoteSource.cancelProfileDeletion()

  override suspend fun clearLocalData() = localSource.clearProfileCache()
}
