package io.chefbook.sdk.profile.impl.data.sources.remote

import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.sdk.profile.impl.data.sources.remote.api.ProfileApiService
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.ConfirmAvatarUploadingRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.GenerateAvatarUploadLinkRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.GetProfileResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.RequestProfileDeletionRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.SetDescriptionRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.SetNameRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.nickname.NicknameApiService
import io.chefbook.sdk.profile.impl.data.sources.remote.nickname.dto.SetNicknameRequest

internal class RemoteProfileSourceImpl(
  private val profileApi: ProfileApiService,
  private val nicknameApi: NicknameApiService,
) : RemoteProfileSource {

  override suspend fun getProfileInfo() =
    profileApi.getProfile().map(GetProfileResponse::toEntity)

  override suspend fun generateAvatarUploading() =
    profileApi.generateAvatarUploadLink().map(GenerateAvatarUploadLinkRequest::toEntity)

  override suspend fun confirmAvatarUploading(avatarId: String) =
    profileApi.setAvatar(ConfirmAvatarUploadingRequest(avatarId)).asEmpty()

  override suspend fun deleteAvatar() =
    profileApi.deleteAvatar().asEmpty()

  override suspend fun checkNicknameAvailability(nickname: String) =
    nicknameApi.checkNicknameAvailability(nickname).map { it.available }

  override suspend fun setNickname(nickname: String) =
    nicknameApi.setNickname(SetNicknameRequest(nickname)).asEmpty()

  override suspend fun setName(firstName: String?, lastName: String?) =
    profileApi.setName(SetNameRequest(firstName, lastName)).asEmpty()

  override suspend fun setDescription(description: String?) =
    profileApi.setDescription(SetDescriptionRequest(description)).asEmpty()

  override suspend fun requestProfileDeletion(password: String, deleteSharedData: Boolean) =
    profileApi.requestProfileDeletion(RequestProfileDeletionRequest(password, deleteSharedData)).asEmpty()

  override suspend fun cancelProfileDeletion() =
    profileApi.cancelProfileDeletion().asEmpty()
}
