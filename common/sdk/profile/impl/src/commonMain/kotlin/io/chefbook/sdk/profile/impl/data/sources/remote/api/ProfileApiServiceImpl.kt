package io.chefbook.sdk.profile.impl.data.sources.remote.api

import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.ConfirmAvatarUploadingRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.GenerateAvatarUploadLinkRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.GetProfileDeletionStatusResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.GetProfileResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.RequestProfileDeletionRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.RequestProfileDeletionResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.SetDescriptionRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.SetNameRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

internal class ProfileApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), ProfileApiService {

  override suspend fun getProfile(): Result<GetProfileResponse> = safeGet(PROFILE_ROUTE)

  override suspend fun getProfile(profileId: String): Result<GetProfileResponse> =
    safeGet("$PROFILE_ROUTE/$profileId")

  override suspend fun generateAvatarUploadLink(): Result<GenerateAvatarUploadLinkRequest> =
    safePost("$PROFILE_ROUTE/avatar")

  override suspend fun setAvatar(body: ConfirmAvatarUploadingRequest): Result<MessageResponse> =
    safePut("$PROFILE_ROUTE/avatar") { setBody(body) }

  override suspend fun deleteAvatar(): Result<MessageResponse> =
    safeDelete("$PROFILE_ROUTE/avatar")

  override suspend fun setName(body: SetNameRequest): Result<MessageResponse> =
    safePut("$PROFILE_ROUTE/name") { setBody(body) }

  override suspend fun setDescription(body: SetDescriptionRequest): Result<MessageResponse> =
    safePut("$PROFILE_ROUTE/description") { setBody(body) }

  override suspend fun getProfileDeletionStatus(): Result<GetProfileDeletionStatusResponse> =
    safeGet("$PROFILE_ROUTE/delete")

  override suspend fun requestProfileDeletion(body: RequestProfileDeletionRequest): Result<RequestProfileDeletionResponse> =
    safePost("$PROFILE_ROUTE/delete") { setBody(body) }

  override suspend fun cancelProfileDeletion(): Result<MessageResponse> =
    safeDelete("$PROFILE_ROUTE/delete")

  companion object {
    private const val PROFILE_ROUTE = "/v1/profile"
  }
}
