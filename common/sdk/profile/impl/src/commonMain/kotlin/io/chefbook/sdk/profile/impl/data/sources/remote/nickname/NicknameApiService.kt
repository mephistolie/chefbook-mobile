package io.chefbook.sdk.profile.impl.data.sources.remote.nickname

import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.nickname.dto.CheckNicknameAvailabilityResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.nickname.dto.SetNicknameRequest

internal interface NicknameApiService {
  suspend fun checkNicknameAvailability(nickname: String): Result<CheckNicknameAvailabilityResponse>

  suspend fun setNickname(body: SetNicknameRequest): Result<MessageResponse>
}
