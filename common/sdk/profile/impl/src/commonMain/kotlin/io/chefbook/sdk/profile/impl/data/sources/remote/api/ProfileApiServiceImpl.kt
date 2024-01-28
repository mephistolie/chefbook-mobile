package io.chefbook.sdk.profile.impl.data.sources.remote.api

import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.dto.ChangeNameRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.dto.GetProfileResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url

internal class ProfileApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), ProfileApiService {

  override suspend fun getProfile(): Result<GetProfileResponse> = safeGet {
    url(PROFILE_ROUTE)
  }

  override suspend fun getProfile(profileId: String): Result<GetProfileResponse> = safeGet {
    url("$PROFILE_ROUTE/$profileId")
  }

  override suspend fun changeName(body: ChangeNameRequest): Result<MessageResponse> = safePost {
    url("$PROFILE_ROUTE/name")
    setBody(body)
  }

  override suspend fun deleteAvatar(): Result<MessageResponse> = safeDelete {
    url("$PROFILE_ROUTE/avatar")
  }

  companion object {
    private const val PROFILE_ROUTE = "/v1/profile"
  }
}
