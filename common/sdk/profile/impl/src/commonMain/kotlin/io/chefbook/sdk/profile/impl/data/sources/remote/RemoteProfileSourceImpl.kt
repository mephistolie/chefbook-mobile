package io.chefbook.sdk.profile.impl.data.sources.remote

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.sdk.core.api.internal.data.models.PictureUploading
import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.impl.data.sources.common.dto.ProfileSerializable
import io.chefbook.sdk.profile.impl.data.sources.remote.api.ProfileApiService
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.ConfirmAvatarUploadingRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.GenerateAvatarUploadLinkRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.RequestProfileDeletionRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.SetDescriptionRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.SetNameRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.nickname.NicknameApiService
import io.chefbook.sdk.profile.impl.data.sources.remote.api.nickname.dto.SetNicknameRequest

internal class RemoteProfileSourceImpl(
  private val profileApi: ProfileApiService,
  private val nicknameApi: NicknameApiService,
) : RemoteProfileSource {

  override suspend fun getProfile(): Result<Profile> =
    profileApi.getProfile().map(ProfileSerializable::toEntity)

  override suspend fun generateAvatarUploading(): Result<PictureUploading> =
    profileApi.generateAvatarUploadLink().map(GenerateAvatarUploadLinkRequest::toEntity)

  override suspend fun confirmAvatarUploading(avatarId: String): EmptyResult =
    profileApi.setAvatar(ConfirmAvatarUploadingRequest(avatarId)).asEmpty()

  override suspend fun deleteAvatar(): EmptyResult =
    profileApi.deleteAvatar().asEmpty()

  override suspend fun checkNicknameAvailability(nickname: String): Result<Boolean> =
    nicknameApi.checkNicknameAvailability(nickname).map { it.available }

  override suspend fun setNickname(nickname: String): EmptyResult =
    nicknameApi.setNickname(SetNicknameRequest(nickname)).asEmpty()

  override suspend fun setName(firstName: String?, lastName: String?): EmptyResult =
    profileApi.setName(SetNameRequest(firstName, lastName)).asEmpty()

  override suspend fun setDescription(description: String?): EmptyResult =
    profileApi.setDescription(SetDescriptionRequest(description)).asEmpty()

  override suspend fun requestProfileDeletion(
    password: String,
    deleteSharedData: Boolean,
  ): EmptyResult =
    profileApi.requestProfileDeletion(RequestProfileDeletionRequest(password, deleteSharedData))
      .asEmpty()

  override suspend fun cancelProfileDeletion(): EmptyResult =
    profileApi.cancelProfileDeletion().asEmpty()
}
