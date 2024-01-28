package io.chefbook.sdk.auth.impl.data.sources.remote.services.nickname

import io.chefbook.sdk.auth.impl.data.sources.remote.services.nickname.dto.CheckNicknameRequest
import io.chefbook.sdk.auth.impl.data.sources.remote.services.nickname.dto.CheckNicknameResponse
import io.chefbook.sdk.auth.impl.data.sources.remote.services.nickname.dto.SetNicknameRequest
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse

internal interface NicknameApiService {
  suspend fun checkNickname(body: CheckNicknameRequest): Result<CheckNicknameResponse>

  suspend fun setNickname(body: SetNicknameRequest): Result<MessageResponse>
}
