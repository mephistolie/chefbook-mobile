package io.chefbook.sdk.profile.impl.data.sources.remote.api

import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.chefbook.sdk.profile.impl.data.sources.remote.dto.ChangeNameRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.dto.GetProfileResponse

internal interface ProfileApiService {

  suspend fun getProfile(): Result<GetProfileResponse>

  suspend fun getProfile(profileId: String): Result<GetProfileResponse>

  suspend fun changeName(body: ChangeNameRequest): Result<MessageResponse>

  suspend fun deleteAvatar(): Result<MessageResponse>
}
