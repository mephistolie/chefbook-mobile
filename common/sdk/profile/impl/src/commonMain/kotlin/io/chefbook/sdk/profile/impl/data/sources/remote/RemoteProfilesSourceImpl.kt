package io.chefbook.sdk.profile.impl.data.sources.remote

import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.impl.data.sources.remote.api.ProfilesApiService
import io.chefbook.sdk.profile.impl.data.sources.common.dto.ProfileSerializable

internal class RemoteProfilesSourceImpl(
  private val profileApi: ProfilesApiService,
) : RemoteProfilesSource {

  override suspend fun getProfile(profileId: String): Result<Profile> =
    profileApi.getProfile(profileId).map(ProfileSerializable::toEntity)
}
