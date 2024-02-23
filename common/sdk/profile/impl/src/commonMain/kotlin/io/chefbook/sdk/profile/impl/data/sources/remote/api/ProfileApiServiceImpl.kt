package io.chefbook.sdk.profile.impl.data.sources.remote.api

import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.ConfirmAvatarUploading
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.GenerateAvatarUploadLinkResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.dto.GetProfileResponse
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

  override suspend fun generateAvatarUploadLink(): Result<GenerateAvatarUploadLinkResponse> =
    safePost("$PROFILE_ROUTE/avatar")

  override suspend fun setAvatar(body: ConfirmAvatarUploading): Result<MessageResponse> =
    safePut("$PROFILE_ROUTE/avatar") { setBody(body) }

  override suspend fun deleteAvatar(): Result<MessageResponse> =
    safeDelete("$PROFILE_ROUTE/avatar")

  override suspend fun setName(body: SetNameRequest): Result<MessageResponse> =
    safePut("$PROFILE_ROUTE/name") { setBody(body) }

  override suspend fun setDescription(body: SetDescriptionRequest): Result<MessageResponse> =
    safePut("$PROFILE_ROUTE/description") { setBody(body) }

  companion object {
    private const val PROFILE_ROUTE = "/v1/profile"
  }
}
