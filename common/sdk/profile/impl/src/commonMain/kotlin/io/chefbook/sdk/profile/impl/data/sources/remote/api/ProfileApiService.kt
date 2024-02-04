package io.chefbook.sdk.profile.impl.data.sources.remote.api

import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.ConfirmAvatarUploading
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.GenerateAvatarUploadLinkResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.GetProfileResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.SetDescriptionRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.SetNameRequest

internal interface ProfileApiService {

  suspend fun getProfile(): Result<GetProfileResponse>

  suspend fun getProfile(profileId: String): Result<GetProfileResponse>

  suspend fun generateAvatarUploadLink(): Result<GenerateAvatarUploadLinkResponse>

  suspend fun setAvatar(body: ConfirmAvatarUploading): Result<MessageResponse>

  suspend fun deleteAvatar(): Result<MessageResponse>

  suspend fun setName(body: SetNameRequest): Result<MessageResponse>

  suspend fun setDescription(body: SetDescriptionRequest): Result<MessageResponse>
}
