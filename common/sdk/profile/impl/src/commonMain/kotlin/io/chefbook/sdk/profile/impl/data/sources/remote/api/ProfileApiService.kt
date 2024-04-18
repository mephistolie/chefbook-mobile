package io.chefbook.sdk.profile.impl.data.sources.remote.api

import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.ConfirmAvatarUploadingRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.GenerateAvatarUploadLinkRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.GetProfileDeletionStatusResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.GetProfileResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.RequestProfileDeletionRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.RequestProfileDeletionResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.SetDescriptionRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.SetNameRequest

internal interface ProfileApiService {

  suspend fun getProfile(): Result<GetProfileResponse>

  suspend fun getProfile(profileId: String): Result<GetProfileResponse>

  suspend fun generateAvatarUploadLink(): Result<GenerateAvatarUploadLinkRequest>

  suspend fun setAvatar(body: ConfirmAvatarUploadingRequest): Result<MessageResponse>

  suspend fun deleteAvatar(): Result<MessageResponse>

  suspend fun setName(body: SetNameRequest): Result<MessageResponse>

  suspend fun setDescription(body: SetDescriptionRequest): Result<MessageResponse>

  suspend fun getProfileDeletionStatus(): Result<GetProfileDeletionStatusResponse>

  suspend fun requestProfileDeletion(body: RequestProfileDeletionRequest): Result<RequestProfileDeletionResponse>

  suspend fun cancelProfileDeletion(): Result<MessageResponse>
}
