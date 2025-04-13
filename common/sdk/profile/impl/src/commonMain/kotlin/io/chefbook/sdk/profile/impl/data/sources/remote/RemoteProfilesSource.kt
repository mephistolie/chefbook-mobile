package io.chefbook.sdk.profile.impl.data.sources.remote

import io.chefbook.sdk.profile.api.external.domain.entities.Profile

interface RemoteProfilesSource {

  suspend fun getProfile(profileId: String): Result<Profile>
}
