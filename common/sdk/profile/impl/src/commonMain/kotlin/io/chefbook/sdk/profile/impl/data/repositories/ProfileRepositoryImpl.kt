package io.chefbook.sdk.profile.impl.data.repositories

import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.coroutines.CoroutineScopes
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.result.onSuccess
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.auth.api.internal.data.repositories.SessionRepository
import io.chefbook.sdk.file.api.internal.data.repositories.FileRepository
import io.chefbook.sdk.file.api.internal.images.ImageCompressor
import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.api.internal.data.repositories.ProfileRepository
import io.chefbook.sdk.profile.impl.data.sources.common.ProfileSource
import io.chefbook.sdk.profile.impl.data.sources.local.LocalProfilesSource
import io.chefbook.sdk.profile.impl.data.sources.remote.RemoteProfileSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext

internal class ProfileRepositoryImpl(
  private val profileId: String,
  private val localSource: ProfileSource,
  private val remoteSource: RemoteProfileSource,
  private val pulledProfilesRepository: PulledProfilesRepository,
  private val sessionRepository: SessionRepository,
  private val files: FileRepository,
  private val compressor: ImageCompressor,
  private val dispatchers: AppDispatchers,
  localProfilesSource: LocalProfilesSource,
  scopes: CoroutineScopes,
) : ProfileRepository {

  private val profileFlow =
    localProfilesSource.observeProfiles().map { profiles ->
      pulledProfilesRepository.pullProfileAsync(profileId)
      profiles[profileId]
    }
      .distinctUntilChanged()
      .shareIn(scopes.repository, SharingStarted.Lazily, replay = 1)

  override fun observeProfile(): Flow<Profile?> = profileFlow

  override suspend fun getProfile(): Result<Profile> {
    profileFlow.first()?.let { profile -> Result.success(profile) }

    val localResult = localSource.getProfile()
    if (localResult.isSuccess) return localResult

    return remoteSource.getProfile()
      .onSuccess(pulledProfilesRepository::cacheProfile)
  }

  override suspend fun refreshProfile(): EmptyResult {
    if (!sessionRepository.isSessionOnline()) return successResult

    return remoteSource.getProfile()
      .onSuccess(pulledProfilesRepository::cacheProfile)
      .asEmpty()
  }

  override suspend fun uploadAvatar(path: String): EmptyResult =
    withContext(dispatchers.io) {
      val targetSource =
        if (sessionRepository.isSessionOnline()) remoteSource else localSource
      val uploadingResult = targetSource.generateAvatarUploading()
        .onFailure { return@withContext Result.failure(it) }
      val uploading = uploadingResult.getOrThrow()

      val compressedPath = compressor.compressImage(
        path = path,
        width = 512,
        height = 512,
        maxFileSize = uploading.maxSize,
      ).onFailure { return@withContext Result.failure(it) }

      val fileResult =
        files.getFile(compressedPath.getOrThrow())
          .onFailure { return@withContext Result.failure(it) }

      val file = fileResult.getOrThrow()

      files.uploadFile(
        path = uploading.uploadPath,
        file = file,
        meta = uploading.meta,
      ).onFailure { return@withContext Result.failure(it) }

      if (sessionRepository.isSessionOnline()) {
        val remoteResult =
          remoteSource.confirmAvatarUploading(uploading.picturePath)
        if (remoteResult.isFailure) return@withContext remoteResult
      }

      return@withContext localSource.confirmAvatarUploading(uploading.picturePath)
    }

  override suspend fun deleteAvatar(): EmptyResult {
    if (sessionRepository.isSessionOnline()) {
      val remoteResult = remoteSource.deleteAvatar()
      if (remoteResult.isFailure) return remoteResult
    }

    return localSource.deleteAvatar()
  }

  override suspend fun checkNicknameAvailability(nickname: String): Result<Boolean> {
    if (!sessionRepository.isSessionOnline()) return Result.success(true)
    return remoteSource.checkNicknameAvailability(nickname)
  }

  override suspend fun setNickname(nickname: String): EmptyResult {
    if (sessionRepository.isSessionOnline()) {
      val remoteResult = remoteSource.setNickname(nickname)
      if (remoteResult.isFailure) return remoteResult
    }

    return localSource.setNickname(nickname)
  }

  override suspend fun setName(
    firstName: String?,
    lastName: String?
  ): EmptyResult {
    if (sessionRepository.isSessionOnline()) {
      val remoteResult = remoteSource.setName(firstName, lastName)
      if (remoteResult.isFailure) return remoteResult
    }

    return localSource.setName(firstName, lastName)
  }

  override suspend fun setDescription(description: String?): EmptyResult {
    if (sessionRepository.isSessionOnline()) {
      val remoteResult = remoteSource.setDescription(description)
      if (remoteResult.isFailure) return remoteResult
    }

    return localSource.setDescription(description)
  }

  override suspend fun requestProfileDeletion(
    password: String,
    deleteSharedData: Boolean
  ) =
    remoteSource.requestProfileDeletion(password, deleteSharedData)

  override suspend fun cancelProfileDeletion() =
    remoteSource.cancelProfileDeletion()

  override suspend fun clearLocalData(profileId: String) =
    pulledProfilesRepository.clearProfileCache(profileId)
}
