package io.chefbook.sdk.auth.impl.data.sources.remote.services.nickname

import io.chefbook.sdk.auth.impl.data.sources.remote.services.nickname.dto.CheckNicknameRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.nickname.dto.CheckNicknameResponse
import io.chefbook.sdk.auth.impl.data.sources.remote.services.nickname.dto.SetNicknameRequest
import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url

internal class NicknameApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), NicknameApiService {

  override suspend fun checkNickname(body: CheckNicknameRequest): Result<CheckNicknameResponse> =
    safeGet {
      url(NICKNAME_ROUTE)
      setBody(body)
    }

  override suspend fun setNickname(body: SetNicknameRequest): Result<MessageResponse> = safePost {
    url(NICKNAME_ROUTE)
    setBody(body)
  }

  companion object {
    private const val NICKNAME_ROUTE = "/v1/auth/nickname"
  }
}
