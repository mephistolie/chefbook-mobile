package io.chefbook.sdk.profile.impl.data.sources.remote.api.nickname

import io.chefbook.sdk.network.api.internal.service.ApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.nickname.dto.CheckNicknameAvailabilityResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.api.nickname.dto.SetNicknameRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

internal class NicknameApiServiceImpl(
  client: HttpClient,
) : ApiService(client), NicknameApiService {

  override suspend fun checkNicknameAvailability(nickname: String): Result<CheckNicknameAvailabilityResponse> =
    safeGet("$NICKNAME_ROUTE/$nickname")

  override suspend fun setNickname(body: SetNicknameRequest): Result<MessageResponse> =
    safePost(NICKNAME_ROUTE) { setBody(body) }

  companion object {
    private const val NICKNAME_ROUTE = "/v1/auth/nickname"
  }
}
