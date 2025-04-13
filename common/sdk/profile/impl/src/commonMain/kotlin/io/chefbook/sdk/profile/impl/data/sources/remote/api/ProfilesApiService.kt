package io.chefbook.sdk.profile.impl.data.sources.remote.api

import io.chefbook.sdk.profile.impl.data.sources.common.dto.ProfileSerializable

internal interface ProfilesApiService {

  suspend fun getProfile(profileId: String): Result<ProfileSerializable>
}
