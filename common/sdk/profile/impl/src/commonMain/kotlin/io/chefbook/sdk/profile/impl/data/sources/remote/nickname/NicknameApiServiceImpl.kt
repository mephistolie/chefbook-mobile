package io.chefbook.sdk.profile.impl.data.sources.remote.nickname

import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.nickname.dto.CheckNicknameAvailabilityResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.nickname.dto.SetNicknameRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url

internal class NicknameApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), NicknameApiService {

  override suspend fun checkNicknameAvailability(nickname: String): Result<CheckNicknameAvailabilityResponse> =
    safeGet {
      url("$NICKNAME_ROUTE/$nickname")
    }

  override suspend fun setNickname(body: SetNicknameRequest): Result<MessageResponse> = safePost {
    url(NICKNAME_ROUTE)
    setBody(body)
  }

  companion object {
    private const val NICKNAME_ROUTE = "/v1/auth/nickname"
  }
}
