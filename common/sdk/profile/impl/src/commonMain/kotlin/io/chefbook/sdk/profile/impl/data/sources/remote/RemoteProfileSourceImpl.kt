package io.chefbook.sdk.profile.impl.data.sources.remote

import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.result.withCast
import io.chefbook.sdk.profile.impl.data.sources.remote.api.ProfileApiService
import io.chefbook.sdk.profile.impl.data.sources.remote.dto.ChangeNameRequest
import io.chefbook.sdk.profile.impl.data.sources.remote.dto.GetProfileResponse

internal class RemoteProfileSourceImpl(
  private val api: ProfileApiService,
) : RemoteProfileSource {
  override suspend fun getProfileInfo() =
    api.getProfile().withCast(GetProfileResponse::toEntity)

  override suspend fun changeName(username: String) =
    api.changeName(ChangeNameRequest(username)).asEmpty()

  override suspend fun deleteAvatar() =
    api.deleteAvatar().asEmpty()
}
